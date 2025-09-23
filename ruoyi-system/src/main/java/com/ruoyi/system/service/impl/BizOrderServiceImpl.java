package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.CoinType;
import com.ruoyi.common.enums.LogStatus;
import com.ruoyi.common.enums.LogType;
import com.ruoyi.common.enums.OrderStatusEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.OrderNoUtils;
import com.ruoyi.system.domain.BizLog;
import com.ruoyi.system.domain.BizProject;
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
        if (paymentAmount.compareTo(placeDTO.getPaymentAmount()) != 0) {
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
            throw new RuntimeException(e);
        }

        //生成订单
        BizOrder bizOrder = new BizOrder();
        bizOrder.setOrderId(OrderNoUtils.getOrderNo(LogType.PURCHASE));
        bizOrder.setProjectId(projectId);
        bizOrder.setComputePower(placeDTO.getComputePower());
        bizOrder.setUnitPrice(unitPrice);
        bizOrder.setPaymentAmount(paymentAmount);
        bizOrder.setStatus(OrderStatusEnum.PAID.getCode());
        bizOrder.setUserId(placeDTO.getUserId());
        bizOrder.setProjectName(bizProject.getProjectName());
        bizOrder.setWaitingPeriod(bizProject.getWaitingPeriod());
        Date createTime = new Date();
        bizOrder.setCreateTime(createTime);
        bizOrder.setIncomeCycle(bizProject.getIncomeCycle());
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
}
