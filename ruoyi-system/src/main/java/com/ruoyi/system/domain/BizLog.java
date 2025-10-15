package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资金流水对象 biz_log
 *
 * @author Remi
 * @date 2025-09-19
 */
public class BizLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    private Long logId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 发起方ID */
    @Excel(name = "发起方ID")
    private Long fromUserId;

    /** 接收方ID */
    @Excel(name = "接收方ID")
    private Long toUserId;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 三种类型：（aiu,aiux,usdt） */
    @Excel(name = "三种类型：", readConverterExp = "a=iu,aiux,usdt")
    private String coinType;

    /** 变动前的金额 */
    @Excel(name = "变动前的金额")
    private BigDecimal beforeAmount;

    /** 关联ID */
    @Excel(name = "关联ID")
    private String orderNo;

    /** 日志类型 */
    @Excel(name = "日志类型")
    private String logType;

    /**  日志状态 */
    @Excel(name = " 日志状态")
    private String logStatus;

    /** 入账地址 */
    @Excel(name = "入账地址")
    private String address;

    @Excel(name = "手续费")
    private BigDecimal fee;

    @Excel(name = "电费")
    private BigDecimal dailyPowerFee;

    @Excel(name = "txId")
    private String txId;

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getDailyPowerFee() {
        return dailyPowerFee;
    }

    public void setDailyPowerFee(BigDecimal dailyPowerFee) {
        this.dailyPowerFee = dailyPowerFee;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }

    public Long getLogId()
    {
        return logId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setFromUserId(Long fromUserId)
    {
        this.fromUserId = fromUserId;
    }

    public Long getFromUserId()
    {
        return fromUserId;
    }

    public void setToUserId(Long toUserId)
    {
        this.toUserId = toUserId;
    }

    public Long getToUserId()
    {
        return toUserId;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setCoinType(String coinType)
    {
        this.coinType = coinType;
    }

    public String getCoinType()
    {
        return coinType;
    }

    public void setBeforeAmount(BigDecimal beforeAmount)
    {
        this.beforeAmount = beforeAmount;
    }

    public BigDecimal getBeforeAmount()
    {
        return beforeAmount;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setLogType(String logType)
    {
        this.logType = logType;
    }

    public String getLogType()
    {
        return logType;
    }

    public void setLogStatus(String logStatus)
    {
        this.logStatus = logStatus;
    }

    public String getLogStatus()
    {
        return logStatus;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("userId", getUserId())
            .append("fromUserId", getFromUserId())
            .append("toUserId", getToUserId())
            .append("amount", getAmount())
            .append("coinType", getCoinType())
            .append("beforeAmount", getBeforeAmount())
            .append("orderNo", getOrderNo())
            .append("logType", getLogType())
            .append("logStatus", getLogStatus())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("address", getAddress())
            .toString();
    }
}
