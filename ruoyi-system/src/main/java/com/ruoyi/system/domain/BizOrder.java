package com.ruoyi.system.domain;

import java.math.BigDecimal;

import com.ruoyi.common.enums.OrderStatusEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单对象 biz_order
 *
 * @author Remi
 * @date 2025-09-16
 */
public class BizOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private String orderId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 项目ID */
    @Excel(name = "项目ID")
    private Long projectId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 订单算力(T) */
    @Excel(name = "订单算力(T)")
    private BigDecimal computePower;

    /** 单价(U/T) */
    @Excel(name = "单价(U/T)")
    private BigDecimal unitPrice;

    /** 支付金额(U) */
    @Excel(name = "支付金额(U)")
    private BigDecimal paymentAmount;

    /** 订单状态（paid:已支付, earning:收益中, power_arrears:电费欠费, ended:收益结束） */
    @Excel(name = "订单状态", dictType = "")
    private String status;

    /** 等待期(天) */
    @Excel(name = "等待期(天)")
    private Long waitingPeriod;

    /** 收益周期(天) */
    @Excel(name = "收益周期(天)")
    private Long incomeCycle;

    /**
     * 分红模式
     */
    private Long shareModeId;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public Long getShareModeId() {
        return shareModeId;
    }

    public void setShareModeId(Long shareModeId) {
        this.shareModeId = shareModeId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getProjectId()
    {
        return projectId;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setComputePower(BigDecimal computePower)
    {
        this.computePower = computePower;
    }

    public BigDecimal getComputePower()
    {
        return computePower;
    }

    public void setUnitPrice(BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setPaymentAmount(BigDecimal paymentAmount)
    {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPaymentAmount()
    {
        return paymentAmount;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setWaitingPeriod(Long waitingPeriod)
    {
        this.waitingPeriod = waitingPeriod;
    }

    public Long getWaitingPeriod()
    {
        return waitingPeriod;
    }

    public void setIncomeCycle(Long incomeCycle)
    {
        this.incomeCycle = incomeCycle;
    }

    public Long getIncomeCycle()
    {
        return incomeCycle;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("userId", getUserId())
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("computePower", getComputePower())
            .append("unitPrice", getUnitPrice())
            .append("paymentAmount", getPaymentAmount())
            .append("status", getStatus())
            .append("waitingPeriod", getWaitingPeriod())
            .append("incomeCycle", getIncomeCycle())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .toString();
    }


    public boolean isEarning() {
        if (this.getStatus() == null) {
            return false;
        }
        return this.getStatus().equals(OrderStatusEnum.EARNING.getCode());
    }
}
