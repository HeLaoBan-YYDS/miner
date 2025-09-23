package com.ruoyi.task;


import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.enums.CoinType;
import com.ruoyi.common.enums.LogStatus;
import com.ruoyi.common.enums.LogType;
import com.ruoyi.common.enums.OrderStatusEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.BizLog;
import com.ruoyi.system.domain.BizOrder;
import com.ruoyi.system.mapper.BizLogMapper;
import com.ruoyi.system.service.IBizLogService;
import com.ruoyi.system.service.IBizOrderService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    @Transactional(rollbackFor = Exception.class)
    public void sendIncome() {

        Date now = new Date();
        //查询今天已发的收益
        Date startOfDay = DateUtil.beginOfDay(now);
        Date endOfDay = DateUtil.endOfDay(now);

        BizLog bizLogCondition = new BizLog();
        bizLogCondition.setLogType(LogType.INCOME.getCode());
        bizLogCondition.getParams().put("startTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startOfDay));
        bizLogCondition.getParams().put("endTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, endOfDay));
        List<BizLog> issuedRewards = bizLogService.selectBizLogList(bizLogCondition);
        List<String> issuedRewardsOrderNo = issuedRewards.stream().map(BizLog::getOrderNo).collect(Collectors.toList());

        BizOrder orderCondition = new BizOrder();
        orderCondition.setDelFlag("0");
        List<BizOrder> bizOrders = orderService.selectBizOrderList(orderCondition);
        if (CollectionUtils.isEmpty(bizOrders)) {
            log.info("没有找到任何订单，跳过收益发放");
            return;
        }
        List<BizOrder> bizOrderList = bizOrders.stream()
                .filter(order -> !OrderStatusEnum.ENDED.getCode().equals(order.getStatus()))
                .collect(Collectors.toList());

        for (BizOrder bizOrder : bizOrderList) {

            Date createTime = bizOrder.getCreateTime();
            Long waitingPeriod = bizOrder.getWaitingPeriod();
            Long userId = bizOrder.getUserId();

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


                String dailyYieldPerTStr = configService.selectConfigByKey("daily_per_t_yield");
                //每日产出单位是比特
                BigDecimal dailyYieldPerT = new BigDecimal(dailyYieldPerTStr);
                BigDecimal income = dailyYieldPerT.multiply(bizOrder.getComputePower());

                //发放收益
                try {
                    userService.updateAccount(userId,income, CoinType.BTC);
                } catch (Exception e) {
                    log.error("发放收益失败,订单号:{},用户ID:{},收益:{}", bizOrder.getOrderId(), userId, income);
                    continue;
                }

                //todo 扣电费


                //记录日志
                BizLog bizLog = new BizLog();
                bizLog.setUserId(userId);
                bizLog.setOrderNo(bizOrder.getOrderId());
                bizLog.setLogType(LogType.INCOME.getCode());
                bizLog.setCoinType(CoinType.BTC.getCode());
                bizLog.setAmount(income);
                bizLog.setCreateTime(new Date());
                bizLog.setLogStatus(LogStatus.SUCCESS.getCode());
                bizLogService.insertBizLog(bizLog);
            }
        }
    }


}
