package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.CoinType;
import com.ruoyi.common.enums.LogStatus;
import com.ruoyi.common.enums.LogType;
import com.ruoyi.common.enums.OrderStatusEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.*;
import com.ruoyi.system.domain.BizLog;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.domain.ShareConfig;
import com.ruoyi.system.domain.dto.PlaceDTO;
import com.ruoyi.system.mapper.BizLogMapper;
import com.ruoyi.system.service.IBizProjectService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BizOrderMapper;
import com.ruoyi.system.domain.BizOrder;
import com.ruoyi.system.service.IBizOrderService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单Service业务层处理
 *
 * @author Remi
 * @date 2025-09-16
 */
@Service
public class BizOrderServiceImpl implements IBizOrderService
{
    @Autowired
    private BizOrderMapper bizOrderMapper;

    @Autowired
    private IBizProjectService projectService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private BizLogMapper bizLogMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private BtcPriceUtil btcPriceUtil;

    private static final String TOTAL_INCOME_ = "TOTAL_INCOME_";

    private static final String YESTERDAY_INCOME_ = "YESTERDAY_INCOME_";

    private static final String ORDER_INCOME_ = "ORDER_INCOME_";

    private static final String ORDER_FEE_ = "ORDER_FEE_";

    /**
     * 查询订单
     *
     * @param orderId 订单主键
     * @return 订单
     */
    @Override
    public BizOrder selectBizOrderByOrderId(Long orderId)
    {
        return bizOrderMapper.selectBizOrderByOrderId(orderId);
    }

    /**
     * 查询订单列表
     *
     * @param bizOrder 订单
     * @return 订单
     */
    @Override
    public List<BizOrder> selectBizOrderList(BizOrder bizOrder)
    {
        return bizOrderMapper.selectBizOrderList(bizOrder);
    }

    /**
     * 新增订单
     *
     * @param bizOrder 订单
     * @return 结果
     */
    @Override
    public int insertBizOrder(BizOrder bizOrder)
    {
        bizOrder.setCreateTime(DateUtils.getNowDate());
        return bizOrderMapper.insertBizOrder(bizOrder);
    }

    /**
     * 修改订单
     *
     * @param bizOrder 订单
     * @return 结果
     */
    @Override
    public int updateBizOrder(BizOrder bizOrder)
    {
        bizOrder.setUpdateTime(DateUtils.getNowDate());
        return bizOrderMapper.updateBizOrder(bizOrder);
    }

    /**
     * 批量删除订单
     *
     * @param orderIds 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deleteBizOrderByOrderIds(Long[] orderIds)
    {
        return bizOrderMapper.deleteBizOrderByOrderIds(orderIds);
    }

    /**
     * 删除订单信息
     *
     * @param orderId 订单主键
     * @return 结果
     */
    @Override
    public int deleteBizOrderByOrderId(Long orderId)
    {
        return bizOrderMapper.deleteBizOrderByOrderId(orderId);
    }

    /**
     * 下单
     * @param placeDTO  下单信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void place(PlaceDTO placeDTO) {
        Long projectId = placeDTO.getProjectId();
        BizProject bizProject = projectService.selectBizProjectById(projectId);
        if (bizProject == null) {
            throw new RuntimeException(MessageUtils.message("project.not.exist"));
        }
        BigDecimal unitPrice = bizProject.getUnitPrice();
        BigDecimal paymentAmount = unitPrice.multiply(placeDTO.getComputePower()).setScale(4, RoundingMode.CEILING).negate();
        if (paymentAmount.compareTo(placeDTO.getPaymentAmount().negate()) != 0) {
            throw new RuntimeException(MessageUtils.message("order.amount.error"));
        }

        SysUser user = sysUserService.selectUserById(placeDTO.getUserId());
        if (user == null) {
            throw new RuntimeException(MessageUtils.message("user.not.exists"));
        }

        //扣钱
        try {
            sysUserService.updateAccount(placeDTO.getUserId(), paymentAmount, CoinType.USDT);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        //生成订单
        BizOrder bizOrder = new BizOrder();
        bizOrder.setOrderId(OrderNoUtils.getOrderNo(LogType.PURCHASE));
        bizOrder.setProjectId(projectId);
        bizOrder.setComputePower(placeDTO.getComputePower());
        bizOrder.setUnitPrice(unitPrice);
        bizOrder.setPaymentAmount(paymentAmount.negate());
        bizOrder.setStatus(OrderStatusEnum.PAID.getCode());
        bizOrder.setUserId(placeDTO.getUserId());
        bizOrder.setProjectName(bizProject.getProjectName());
        bizOrder.setWaitingPeriod(bizProject.getWaitingPeriod());
        Date createTime = new Date();
        bizOrder.setCreateTime(createTime);
        bizOrder.setIncomeCycle(bizProject.getIncomeCycle());
        bizOrder.setShareModeId(placeDTO.getShareModeId());
        bizOrderMapper.insertBizOrder(bizOrder);

        //扣款日志
        BizLog bizLog = new BizLog();
        bizLog.setUserId(placeDTO.getUserId());
        bizLog.setAmount(paymentAmount);
        bizLog.setOrderNo(bizOrder.getOrderId());
        bizLog.setLogType(LogType.PURCHASE.getCode());
        bizLog.setCreateTime(createTime);
        bizLog.setLogStatus(LogStatus.SUCCESS.getCode());
        bizLog.setCoinType(CoinType.USDT.getCode());
        bizLog.setBeforeAmount(user.getAccount());
        bizLogMapper.insertBizLog(bizLog);
    }


    /**
     * 每天早上8点发放的收益就是用户昨天矿机的收益，所以这里查询昨日收益,其实系统时间是今天早上8点后的收益
     * @param userId 用户ID
     * @return 昨日收益
     */
    @Override
    public BigDecimal getYesterdayIncome(Long userId) {
        String redisKey = YESTERDAY_INCOME_ + userId;
        String yIncome = redisCache.getCacheObject(redisKey);
        if (yIncome != null) {
            return new BigDecimal(yIncome);
        }
        Date todayStart = DateUtil.beginOfDay(new Date());
        todayStart = DateUtil.offsetHour(todayStart, 8);
        Date todayEnd = DateUtil.endOfDay(new Date());
        BigDecimal income = getIncomeByRange(userId, todayStart, todayEnd);
        yIncome = income.toString();
        redisCache.setCacheObject(redisKey,yIncome);
        return income;
    }



    @Override
    public BigDecimal getTotalIncome(Long userId) {
        String redisKey = TOTAL_INCOME_ + userId;
        String tIncome = redisCache.getCacheObject(redisKey);
        if (tIncome != null) {
            return new BigDecimal(tIncome);
        }
        BigDecimal income = getIncomeByRange(userId, null, null);
        tIncome = income.toString();
        redisCache.setCacheObject(redisKey,tIncome);
        return income;
    }

    @Override
    public BigDecimal getIncomeUSDTByOrderId(String orderId) {
        String redisKey = ORDER_INCOME_ + orderId;
        String tIncomeUSDT = redisCache.getCacheObject(redisKey);
        if (tIncomeUSDT != null) {
            return new BigDecimal(tIncomeUSDT);
        }
        BigDecimal income = calIncomeBTCByOrderNo(orderId);
        tIncomeUSDT = btcPriceUtil.btcToUsdt(income).toString();
        redisCache.setCacheObject(redisKey,tIncomeUSDT);
        return income;
    }


    @Override
    public BigDecimal getFeeUSDTByOrderId(String orderId) {
        String redisKey = ORDER_FEE_ + orderId;
        String tFee = redisCache.getCacheObject(redisKey);
        if (tFee != null) {
            return new BigDecimal(tFee);
        }
        BigDecimal totalFee = calFeeUSDTByOrderId(orderId);
        tFee = totalFee.toString();
        redisCache.setCacheObject(redisKey,tFee);
        return totalFee;
    }


    private BigDecimal calIncomeBTCByOrderNo(String orderNo) {
        BizLog bizLog = new BizLog();
        bizLog.setLogType(LogType.INCOME.getCode());
        bizLog.setOrderNo(orderNo);
        List<BizLog> bizLogs = bizLogMapper.selectBizLogList(bizLog);
        if (CollUtil.isEmpty(bizLogs)) {
            return BigDecimal.ZERO;
        }
        return bizLogs.stream().map(BizLog::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private BigDecimal calFeeUSDTByOrderId(String orderNo) {
        BizLog bizLog = new BizLog();
        bizLog.setLogType(LogType.POWER_FEE.getCode());
        bizLog.setOrderNo(orderNo);
        List<BizLog> bizLogs = bizLogMapper.selectBizLogList(bizLog);
        if (CollUtil.isEmpty(bizLogs)) {
            return BigDecimal.ZERO;
        }
        return bizLogs.stream().map(BizLog::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).negate();
    }


    private BigDecimal getIncomeByRange(Long userId, Date yesterdayStart, Date yesterdayEnd) {
        BizLog bizLog = new BizLog();
        bizLog.setLogType(LogType.INCOME.getCode());
        bizLog.setUserId(userId);
        if (yesterdayStart !=null){
            bizLog.getParams().put("beginTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, yesterdayStart));
        }
        if (yesterdayEnd !=null){
            bizLog.getParams().put("endTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, yesterdayEnd));
        }
        List<BizLog> bizLogs = bizLogMapper.selectBizLogList(bizLog);
        if (CollUtil.isEmpty(bizLogs)) {
            return BigDecimal.ZERO;
        }
        return bizLogs.stream().map(BizLog::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<ShareConfig> selectMode() {
        List<SysDictData> shareMode = DictUtils.getDictCache("share_mode");
        if (CollUtil.isNotEmpty(shareMode)&&shareMode != null) {
            return shareMode.stream().map(d -> {
                ShareConfig bean = JSONUtil.toBean(d.getDictValue(), ShareConfig.class);
                bean.setId(d.getDictCode());
                return bean;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void innitCache(BizOrder bizOrder) {
        redisCache.deleteObject(YESTERDAY_INCOME_ + bizOrder.getUserId());
        redisCache.deleteObject(TOTAL_INCOME_ + bizOrder.getUserId());
        redisCache.deleteObject(ORDER_INCOME_ + bizOrder.getOrderId());
        redisCache.deleteObject(ORDER_FEE_ + bizOrder.getOrderId());

        this.getYesterdayIncome(bizOrder.getUserId());
        this.getTotalIncome(bizOrder.getUserId());
        this.getIncomeUSDTByOrderId(bizOrder.getOrderId());
        this.getFeeUSDTByOrderId(bizOrder.getOrderId());
    }


}
