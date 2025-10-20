package com.ruoyi.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.xml.crypto.Data;

/**
 * 项目基本信息对象 biz_project
 *
 * @author Remi
 * @date 2025-09-16
 */
@ApiModel("项目列表响应")
public class ProjectVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    @ApiModelProperty("项目名称")
    private String projectName;

    /** 算力(T) */
    @Excel(name = "算力(T)")
    @ApiModelProperty("算力(T)")
    private BigDecimal computePower;

    /** 功耗(W) */
    @Excel(name = "功耗(KW)")
    @ApiModelProperty("功耗(KW)")
    private BigDecimal powerConsumption;

    /** 单价(U/T) */
    @Excel(name = "单价(U/T)")
    @ApiModelProperty("单价(U/T)")
    private BigDecimal unitPrice;

    /** 最小起购(T) */
    @Excel(name = "最小起购(T)")
    @ApiModelProperty("最小起购(T)")
    private BigDecimal minPurchase;

    /** 等待期(天) */
    @Excel(name = "等待期(天)")
    @ApiModelProperty("等待期(天)")
    private Long waitingPeriod;

    /** 收益周期(天) */
    @Excel(name = "收益周期(天)")
    @ApiModelProperty("收益周期(天)")
    private Long incomeCycle;

    /** 项目简介 */
    @Excel(name = "项目简介图片")
    @ApiModelProperty("项目简介图片")
    private String intro;

    /** 项目描述 */
    @Excel(name = "项目描述")
    @ApiModelProperty("项目描述")
    private String description;

    @ApiModelProperty("APR")
    private BigDecimal apr;

    @ApiModelProperty("预计回本(天)")
    private BigDecimal returnAmount;

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public BigDecimal getApr() {
        return apr;
    }

    public void setApr(BigDecimal apr) {
        this.apr = apr;
    }

    public String getStartMiningTime() {
        if (waitingPeriod == null || incomeCycle == null){
            return null;
        }
        return DateUtil.offsetDay(new Date(), waitingPeriod.intValue()).toString();
    }

    public String getEndMiningTime() {
        if (waitingPeriod == null || incomeCycle == null){
            return null;
        }
        return DateUtil.offsetDay(new Date(), incomeCycle.intValue() + waitingPeriod.intValue()).toString();
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
