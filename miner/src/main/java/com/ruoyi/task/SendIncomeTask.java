package com.ruoyi.task;


import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.BizLog;
import com.ruoyi.system.domain.BizOrder;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private Map<Long, BizProject> projectMap = new HashMap<>();

    //每日产出单位是比特
   private String dailyYieldPerTStr = "0";

   //单位电价
   private BigDecimal powerFee = null;


    public void sendIncome() {

        Date now = new Date();
        //查询今天已发的收益日志
        Date startOfDay = DateUtil.beginOfDay(now);
        Date endOfDay = DateUtil.endOfDay(now);
        BizLog bizLogCondition = new BizLog();
        bizLogCondition.setLogType(LogType.INCOME.getCode());
        bizLogCondition.getParams().put("startTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startOfDay));
        bizLogCondition.getParams().put("endTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, endOfDay));
        List<BizLog> issuedRewards = bizLogService.selectBizLogList(bizLogCondition);
        List<String> issuedRewardsOrderNo = issuedRewards.stream().map(BizLog::getOrderNo).collect(Collectors.toList());

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
        dailyYieldPerTStr = configService.selectConfigByKey("daily_per_t_yield");

        powerFee = new BigDecimal(configService.selectConfigByKey("daily_power_fee"));

        if (ObjectUtils.isEmpty(dailyYieldPerTStr) || new BigDecimal(dailyYieldPerTStr).compareTo(BigDecimal.ZERO) <= 0) {
            String msg = "daily_per_t_yield 配置错误";
            throw new ServiceException(msg);
        }

        if (ObjectUtils.isEmpty(powerFee) || powerFee.compareTo(BigDecimal.ZERO) <= 0) {
            String msg = "daily_power_fee 配置错误";
            throw new ServiceException(msg);
        }

        SendIncomeTask sendIncomeTask = SpringUtils.getBean(SendIncomeTask.class);
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
                //到达收益开始时间，更新订单状态为收益中
                bizOrder.setStatus(OrderStatusEnum.EARNING.getCode());
                orderService.updateBizOrder(bizOrder);
            }

            if (bizOrder.isEarning()){

               if (issuedRewardsOrderNo.contains(bizOrder.getOrderId())){
                   //今天已发过收益，跳过
                   continue;
               }

                sendIncomeTask.handlerOrderIncome(bizOrder);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handlerOrderIncome(BizOrder bizOrder) {
        //扣除电费
        BigDecimal orderComputePower = deductElectricityFee(bizOrder);

        //扣电费日志
        savePowerFeeLog(bizOrder, orderComputePower);

        //发放收益
        BigDecimal income = sendIncome(bizOrder);

        //记录奖励日志
        saveIncomeLog(bizOrder, income);
    }

    private void savePowerFeeLog(BizOrder bizOrder, BigDecimal orderComputePower) {
        BizLog powerFeeLog = new BizLog();
        powerFeeLog.setUserId(bizOrder.getUserId());
        powerFeeLog.setOrderNo(bizOrder.getOrderId());
        powerFeeLog.setLogType(LogType.POWER_FEE.getCode());
        powerFeeLog.setCoinType(CoinType.USDT.getCode());
        powerFeeLog.setAmount(orderComputePower);
        powerFeeLog.setCreateTime(new Date());
        powerFeeLog.setLogStatus(LogStatus.SUCCESS.getCode());
        bizLogService.insertBizLog(powerFeeLog);
    }

    private void saveIncomeLog(BizOrder bizOrder, BigDecimal income) {
        BizLog bizLog = new BizLog();
        bizLog.setUserId(bizOrder.getUserId());
        bizLog.setOrderNo(bizOrder.getOrderId());
        bizLog.setLogType(LogType.INCOME.getCode());
        bizLog.setCoinType(CoinType.BTC.getCode());
        bizLog.setAmount(income);
        bizLog.setCreateTime(new Date());
        bizLog.setLogStatus(LogStatus.SUCCESS.getCode());
        bizLogService.insertBizLog(bizLog);
    }

    private BigDecimal sendIncome(BizOrder bizOrder) {
        Long userId = bizOrder.getUserId();
        BigDecimal dailyYieldPerT = new BigDecimal(dailyYieldPerTStr);
        BigDecimal income = dailyYieldPerT.multiply(bizOrder.getComputePower());
        try {
            userService.updateAccount(userId,income, CoinType.BTC);
        } catch (Exception e) {
            String msg = String.format("发放收益失败,订单号:%s,用户ID:%s,收益:%s", bizOrder.getOrderId(), userId, income);
            throw new ServiceException(msg);
        }
        return income;
    }

    private BigDecimal deductElectricityFee(BizOrder bizOrder) {
        BizProject bizProject = projectMap.get(bizOrder.getProjectId());

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
                .multiply(powerFee)
                .setScale(8, RoundingMode.DOWN).negate();

        try {
            userService.updateAccount(bizOrder.getUserId(),electricityFee, CoinType.USDT);
        } catch (Exception e) {
            String msg = String.format("扣除电费失败,订单号:%s,用户ID:%s,电费:%s", bizOrder.getOrderId(), bizOrder.getUserId(), electricityFee);
            log.error("发放收益失败,电费不够订单号:{},用户ID:{}", bizOrder.getOrderId(), bizOrder.getUserId());
            bizOrder.setStatus(OrderStatusEnum.POWER_ARREARS.getCode());
            orderService.updateBizOrder(bizOrder);
            throw new ServiceException(msg);
        }

        return orderComputePower;
    }


}
