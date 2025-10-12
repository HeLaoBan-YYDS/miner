package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目基本信息对象 biz_project
 *
 * @author Remi
 * @date 2025-09-16
 */
public class BizProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 算力(T) */
    @Excel(name = "算力(T)")
    private BigDecimal computePower;

    /** 功耗(W) */
    @Excel(name = "功耗(KW)")
    private BigDecimal powerConsumption;

    /** 单价(U/T) */
    @Excel(name = "单价(U/T)")
    private BigDecimal unitPrice;

    /** 最小起购(T) */
    @Excel(name = "最小起购(T)")
    private BigDecimal minPurchase;

    /** 等待期(天) */
    @Excel(name = "等待期(天)")
    private Long waitingPeriod;

    /** 收益周期(天) */
    @Excel(name = "收益周期(天)")
    private Long incomeCycle;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
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

    public void setPowerConsumption(BigDecimal powerConsumption)
    {
        this.powerConsumption = powerConsumption;
    }

    public BigDecimal getPowerConsumption()
    {
        return powerConsumption;
    }

    public void setUnitPrice(BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setMinPurchase(BigDecimal minPurchase)
    {
        this.minPurchase = minPurchase;
    }

    public BigDecimal getMinPurchase()
    {
        return minPurchase;
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

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
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
            .append("id", getId())
            .append("projectName", getProjectName())
            .append("computePower", getComputePower())
            .append("powerConsumption", getPowerConsumption())
            .append("unitPrice", getUnitPrice())
            .append("minPurchase", getMinPurchase())
            .append("waitingPeriod", getWaitingPeriod())
            .append("incomeCycle", getIncomeCycle())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
