package com.ruoyi.task;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.BtcPriceUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component("sendIncomeTask")
@Slf4j
public class SendIncomeTask {

    @Autowired
    private IBizLogService bizLogService;

    @Autowired
    private IBizOrderService orderService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IBizProjectService projectService;

    @Autowired
    @Lazy
    private SendIncomeTask sendIncomeTask;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private BtcPriceUtil btcPriceUtil;


    private Map<Long, BizProject> projectMap = new HashMap<>();

    private Map<Long, ShareConfig> shareModelMap = new HashMap<>();

    //每日产出单位是比特
   private BigDecimal dailyYieldPerTStr = BigDecimal.ZERO;

   //单位电价
   private BigDecimal powerFee = null;



    public void sendIncome() {

        Date now = new Date();
        //查询今天已发的收益日志
        Date startOfDay = DateUtil.beginOfDay(now);
        Date endOfDay = DateUtil.endOfDay(now);
        BizLog bizLogCondition = new BizLog();
        bizLogCondition.setLogType(LogType.INCOME.getCode());
        bizLogCondition.getParams().put("beginTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startOfDay));
        bizLogCondition.getParams().put("endTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, endOfDay));
        List<BizLog> issuedRewards = bizLogService.selectBizLogList(bizLogCondition);
        List<String> issuedRewardsOrderNo = issuedRewards.stream().map(BizLog::getOrderNo).collect(Collectors.toList());
        //初始化分销配置
        List<ShareConfig> shareConfigs = orderService.selectMode();
        innitShareModel(shareConfigs);


        //查询订单
        BizOrder orderCondition = new BizOrder();
        orderCondition.setDelFlag("0");
        List<BizOrder> bizOrders = orderService.selectBizOrderList(orderCondition);
        if (CollectionUtils.isEmpty(bizOrders)) {
            log.info("没有找到任何订单，跳过收益发放");
            return;
        }

        //获取在售订单
        BizProject project = new BizProject();
        project.setStatus(ProjectStatus.SUCCESS.getCode());
        List<BizProject> projectList = projectService.selectBizProjectList(project);
        projectMap = projectList.stream().collect(Collectors.toMap(BizProject::getId, p -> p));


        //过滤不满足条件的订单
        List<BizOrder> bizOrderList = bizOrders.stream()
                .filter(order -> !OrderStatusEnum.ENDED.getCode().equals(order.getStatus()))
                .collect(Collectors.toList());

        //每日产出单位是比特
        dailyYieldPerTStr = new BigDecimal(configService.selectConfigByKey("daily_per_t_yield"));

        powerFee = new BigDecimal(configService.selectConfigByKey("daily_power_fee"));

        if (ObjectUtils.isEmpty(dailyYieldPerTStr) || dailyYieldPerTStr.compareTo(BigDecimal.ZERO) <= 0) {
            String msg = "daily_per_t_yield 配置错误";
            throw new ServiceException(msg);
        }

        if (ObjectUtils.isEmpty(powerFee) || powerFee.compareTo(BigDecimal.ZERO) <= 0) {
            String msg = "daily_power_fee 配置错误";
            throw new ServiceException(msg);
        }


        if (CollectionUtils.isEmpty(bizOrderList)) {
            throw new ServiceException("sendIncomeTaskBean 获取失败");
        }

        for (BizOrder bizOrder : bizOrderList) {

            Date createTime = bizOrder.getCreateTime();
            Long waitingPeriod = bizOrder.getWaitingPeriod();

            //收益中的订单
            if (!bizOrder.isEarning()) {
                Date earningStartTime = DateUtil.offsetDay(createTime, Math.toIntExact(waitingPeriod));
                //非收益中订单，判断是否到达收益开始时间
                if (now.before(earningStartTime)) {
                    //未到达收益开始时间，跳过
                    continue;
                }


                if (now.after(DateUtil.offsetDay(earningStartTime, Math.toIntExact(bizOrder.getIncomeCycle())))) {
                    //超过收益结束时间，更新订单状态为已结束
                    bizOrder.setStatus(OrderStatusEnum.ENDED.getCode());
                    orderService.updateBizOrder(bizOrder);
                    continue;
                }

                //到达收益开始时间，更新订单状态为收益中
                bizOrder.setStatus(OrderStatusEnum.EARNING.getCode());
                orderService.updateBizOrder(bizOrder);
            }

            if (bizOrder.isEarning()){

               if (issuedRewardsOrderNo.contains(bizOrder.getOrderId())){
                   //今天已发过收益，跳过
                   continue;
               }

                try {
                    sendIncomeTask.handlerOrderIncome(bizOrder);
                } catch (Exception e) {
                    log.error("订单收益发放失败，订单号：{}", bizOrder.getOrderId(), e);
                    continue;
                }
            }
        }
    }

    private void innitShareModel(List<ShareConfig> shareConfigs) {
        this.shareModelMap = shareConfigs.stream().collect(Collectors.toMap(ShareConfig::getId, s -> s));
    }

    @Transactional(rollbackFor = Exception.class)
    public void handlerOrderIncome(BizOrder bizOrder) {
        //扣除电费
        BigDecimal electricityFee = deductElectricityFee(bizOrder);

        //扣电费日志
        savePowerFeeLog(bizOrder, electricityFee);

        //发放收益
        IncomeInfo income = sendIncome(bizOrder,electricityFee);

        //记录奖励日志
        saveIncomeLog(bizOrder, income,electricityFee);

        //清理缓存
        CompletableFuture.runAsync(() -> {
            try {
                orderService.innitCache(bizOrder);
            } catch (Exception e) {
                log.error("清理缓存失败", e);
            }
        });
    }

    private void savePowerFeeLog(BizOrder bizOrder, BigDecimal orderPowerFee) {
        SysUser user = sysUserService.selectUserById(bizOrder.getUserId());
        BigDecimal beforeAmount = user.getAccount().add(orderPowerFee.negate());
        BizLog powerFeeLog = new BizLog();
        powerFeeLog.setUserId(bizOrder.getUserId());
        powerFeeLog.setOrderNo(bizOrder.getOrderId());
        powerFeeLog.setLogType(LogType.POWER_FEE.getCode());
        powerFeeLog.setCoinType(CoinType.USDT.getCode());
        powerFeeLog.setAmount(orderPowerFee);
        powerFeeLog.setCreateTime(new Date());
        powerFeeLog.setLogStatus(LogStatus.SUCCESS.getCode());
        powerFeeLog.setBeforeAmount(beforeAmount);
        bizLogService.insertBizLog(powerFeeLog);
    }

    private void saveIncomeLog(BizOrder bizOrder, IncomeInfo incomeInfo, BigDecimal electricityFee) {
        BigDecimal income = incomeInfo.getUserIncome();
        SysUser user = sysUserService.selectUserById(bizOrder.getUserId());
        BigDecimal beforeAmount = user.getAccount().subtract(income);
        if (BigDecimal.ZERO.compareTo(beforeAmount) > 0){
            beforeAmount = BigDecimal.ZERO;
        }
        BizLog bizLog = new BizLog();
        bizLog.setUserId(bizOrder.getUserId());
        bizLog.setOrderNo(bizOrder.getOrderId());
        bizLog.setLogType(LogType.INCOME.getCode());
        bizLog.setCoinType(CoinType.BTC.getCode());
        bizLog.setAmount(income);
        bizLog.setCreateTime(new Date());
        bizLog.setBeforeAmount(beforeAmount);
        bizLog.setLogStatus(LogStatus.SUCCESS.getCode());
        bizLog.setRemark(JSONUtil.toJsonStr(incomeInfo));
        bizLog.setDailyPowerFee(electricityFee);
        bizLogService.insertBizLog(bizLog);
    }

    private IncomeInfo sendIncome(BizOrder bizOrder, BigDecimal electricityFee) {
        if (bizOrder.getShareModeId() == null) {
            String msg = String.format("订单分红模式未设置，订单号:%s", bizOrder.getOrderId());
            throw new ServiceException(msg);
        }

        ShareConfig shareConfig = shareModelMap.get(bizOrder.getShareModeId());
        if (ObjectUtils.isEmpty(shareConfig)) {
            String msg = String.format("订单分红模式不存在，订单号:%s,分红模式ID:%s", bizOrder.getOrderId(), bizOrder.getShareModeId());
            throw new ServiceException(msg);
        }

        //用户收益
        BigDecimal dailyIncome = dailyYieldPerTStr.multiply(bizOrder.getComputePower());
        //用户成本
        Boolean isReturn = isReturn(bizOrder);

        IncomeInfo incomeInfo = shareConfig.calUserIncome(dailyIncome,btcPriceUtil.usdtToBtc(electricityFee),isReturn);

        Long userId = bizOrder.getUserId();

        try {
            userService.updateAccount(userId,incomeInfo.getUserIncome(), CoinType.BTC);
        } catch (Exception e) {
            String msg = String.format("发放收益失败,订单号:%s,用户ID:%s,收益:%s", bizOrder.getOrderId(), userId, incomeInfo);
            throw new ServiceException(msg);
        }
        return incomeInfo;
    }

    /**
     * 用户是否回本
     * @param bizOrder
     * @return
     */
    private boolean isReturn(BizOrder bizOrder) {
        BigDecimal paymentAmount = bizOrder.getPaymentAmount();

        BigDecimal orderTotalIncomeUSDT = orderService.getIncomeUSDTByOrderId(bizOrder.getOrderId());

        BigDecimal totalFeeUSDT = orderService.getFeeUSDTByOrderId(bizOrder.getOrderId());

        BigDecimal orderTotalProfit = orderTotalIncomeUSDT.subtract(totalFeeUSDT);

        return orderTotalProfit.compareTo(paymentAmount) >= 0;
    }

    private BigDecimal deductElectricityFee(BizOrder bizOrder) {
        BizProject bizProject = projectMap.get(bizOrder.getProjectId());

        ShareConfig shareConfig = shareModelMap.get(bizOrder.getShareModeId());

        if (ObjectUtils.isEmpty(shareConfig)) {
            String msg = String.format("订单分红模式不存在，订单号:%s,分红模式ID:%s", bizOrder.getOrderId(), bizOrder.getShareModeId());
            throw new ServiceException(msg);
        }

        if (ObjectUtils.isEmpty(bizProject)){
            String msg = String.format("项目不存在，订单号:%s,项目ID:%s", bizOrder.getOrderId(), bizOrder.getProjectId());
            throw new ServiceException(msg);
        }

        BigDecimal powerConsumption = bizProject.getPowerConsumption();
        BigDecimal computePower = bizProject.getComputePower();
        BigDecimal orderComputePower = bizOrder.getComputePower();



        //电费
        BigDecimal electricityFee = powerConsumption
                .divide(computePower, 8, RoundingMode.DOWN)
                .multiply(orderComputePower)
                .multiply(shareConfig.getElectricityFee())
                .multiply(new BigDecimal("24")) //24小时
                .setScale(8, RoundingMode.DOWN).negate();

        try {
            userService.updateAccount(bizOrder.getUserId(),electricityFee, CoinType.USDT);
        } catch (Exception e) {
            String msg = String.format("扣除电费失败,订单号:%s,用户ID:%s,电费:%s", bizOrder.getOrderId(), bizOrder.getUserId(), electricityFee);
            log.error("发放收益失败,电费不够订单号:{},用户ID:{}", bizOrder.getOrderId(), bizOrder.getUserId());
            sendIncomeTask.updateOrderToPowerArrears(bizOrder);
            throw new ServiceException(msg);
        }

        return electricityFee;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateOrderToPowerArrears(BizOrder bizOrder) {
        bizOrder.setStatus(OrderStatusEnum.POWER_ARREARS.getCode());
        orderService.updateBizOrder(bizOrder);
    }


}
