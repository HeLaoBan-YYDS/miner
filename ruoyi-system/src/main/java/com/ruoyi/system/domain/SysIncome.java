package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 收益管理对象 sys_income
 *
 * @author Remi
 * @date 2025-09-17
 */
public class SysIncome extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 收益流水ID */
    private Long incomeId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 项目ID */
    @Excel(name = "项目ID")
    private Long projectId;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 收益金额(BTC) */
    @Excel(name = "收益金额(BTC)")
    private BigDecimal incomeAmount;

    /** 订单算力(T) */
    @Excel(name = "订单算力(T)")
    private BigDecimal orderHashrate;

    /** 收益发放日期 */
    private Date issueDate;

    /** 收益发放时间 */
    private Date issueTime;

    /** 完整的收益发放时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完整的收益发放时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issueDatetime;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setIncomeId(Long incomeId)
    {
        this.incomeId = incomeId;
    }

    public Long getIncomeId()
    {
        return incomeId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getOrderId()
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

    public void setIncomeAmount(BigDecimal incomeAmount)
    {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getIncomeAmount()
    {
        return incomeAmount;
    }

    public void setOrderHashrate(BigDecimal orderHashrate)
    {
        this.orderHashrate = orderHashrate;
    }

    public BigDecimal getOrderHashrate()
    {
        return orderHashrate;
    }

    public void setIssueDate(Date issueDate)
    {
        this.issueDate = issueDate;
    }

    public Date getIssueDate()
    {
        return issueDate;
    }

    public void setIssueTime(Date issueTime)
    {
        this.issueTime = issueTime;
    }

    public Date getIssueTime()
    {
        return issueTime;
    }

    public void setIssueDatetime(Date issueDatetime)
    {
        this.issueDatetime = issueDatetime;
    }

    public Date getIssueDatetime()
    {
        return issueDatetime;
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
            .append("incomeId", getIncomeId())
            .append("orderId", getOrderId())
            .append("userId", getUserId())
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("incomeAmount", getIncomeAmount())
            .append("orderHashrate", getOrderHashrate())
            .append("issueDate", getIssueDate())
            .append("issueTime", getIssueTime())
            .append("issueDatetime", getIssueDatetime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
