package com.ruoyi.system.domain;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资金流水对象 biz_log
 *
 * @author Remi
 * @date 2025-09-18
 */
@Setter
@Getter
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

    /** 币种 */
    @Excel(name = "币种")
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
            .toString();
    }
}
