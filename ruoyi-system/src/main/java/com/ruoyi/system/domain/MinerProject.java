package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目管理对象 miner_project
 *
 * @author Remi
 * @date 2025-11-10
 */
public class MinerProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 产品图URL */
    @Excel(name = "产品图URL")
    private String productImage;

    /** 套餐类型 */
    @Excel(name = "套餐类型")
    private String packageType;

    /** 购买限制类型 */
    @Excel(name = "购买限制类型")
    private String purchaseLimit;

    /** 算力购买类型 */
    @Excel(name = "算力购买类型")
    private String computePowerType;

    /** 机器算力(T) */
    @Excel(name = "机器算力(T)")
    private Integer computePower;

    /** 功耗(W) */
    @Excel(name = "功耗(W)")
    private Integer powerConsumption;

    /** 单价(U/T) */
    @Excel(name = "单价(U/T)")
    private BigDecimal unitPrice;

    /** 单位电费(U/kwh) */
    @Excel(name = "单位电费(U/kwh)")
    private BigDecimal unitPowerCost;

    /** 机器单价(T/U) */
    @Excel(name = "机器单价(T/U)")
    private BigDecimal machineUnitPrice;

    /** 最小起购(T) */
    @Excel(name = "最小起购(T)")
    private Integer minPurchase;

    /** 最高购买数量(T) */
    @Excel(name = "最高购买数量(T)")
    private Integer maxPurchase;

    /** 最低首次缴纳天数 */
    @Excel(name = "最低首次缴纳天数")
    private Integer minFirstPaymentDays;

    /** 计划周期(天) */
    @Excel(name = "计划周期(天)")
    private Integer planCycle;

    /** 批次类型 */
    @Excel(name = "批次类型")
    private String batchTagType;

    /** 机器数量 */
    @Excel(name = "机器数量")
    private Long machineCount;

    /** 开售时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开售时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date saleStartTime;

    /** 结售时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结售时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date saleEndTime;

    /** 预计上架时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计上架时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectedOnlineTime;

    /** 矿场负荷（high:高, medium:中, low:低） */
    @Excel(name = "矿场负荷", readConverterExp = "h=igh:高,,m=edium:中,,l=ow:低")
    private String mineLoad;

    /** 矿场位置 */
    @Excel(name = "矿场位置")
    private String mineLocation;

    /** 历史在线率(%) */
    @Excel(name = "历史在线率(%)")
    private BigDecimal historicalOnlineRate;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 排序字段 */
    @Excel(name = "排序字段")
    private Long sort;

    /** 标签 */
    @Excel(name = "标签")
    private String tagType;

    /** 产品类型 */
    @Excel(name = "产品类型")
    private String type;

    /** 预留字段ONE */
    private String reservedFieldOne;

    /** 预留字段TWO */
    private String reservedFieldTwo;

    /** 预留字段Three */
    private String reservedFieldThree;

    /** 单日静态产出(BTC) */
    @Excel(name = "单日静态产出(BTC)")
    private BigDecimal singleDayStaticOutputBtc;

    /** 折合服务费单价 */
    @Excel(name = "折合服务费单价")
    private BigDecimal serviceFeeUnitPrice;

    /** 默认托管天数 */
    @Excel(name = "默认托管天数")
    private Long depositDays;

    /** 矿机残值 */
    @Excel(name = "矿机残值")
    private BigDecimal residualValue;

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

    public void setProductImage(String productImage)
    {
        this.productImage = productImage;
    }

    public String getProductImage()
    {
        return productImage;
    }

    public void setPackageType(String packageType)
    {
        this.packageType = packageType;
    }

    public String getPackageType()
    {
        return packageType;
    }

    public void setPurchaseLimit(String purchaseLimit)
    {
        this.purchaseLimit = purchaseLimit;
    }

    public String getPurchaseLimit()
    {
        return purchaseLimit;
    }

    public void setComputePowerType(String computePowerType)
    {
        this.computePowerType = computePowerType;
    }

    public String getComputePowerType()
    {
        return computePowerType;
    }

    public void setComputePower(Integer computePower)
    {
        this.computePower = computePower;
    }

    public Integer getComputePower()
    {
        return computePower;
    }

    public void setPowerConsumption(Integer powerConsumption)
    {
        this.powerConsumption = powerConsumption;
    }

    public Integer getPowerConsumption()
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

    public void setUnitPowerCost(BigDecimal unitPowerCost)
    {
        this.unitPowerCost = unitPowerCost;
    }

    public BigDecimal getUnitPowerCost()
    {
        return unitPowerCost;
    }

    public void setMachineUnitPrice(BigDecimal machineUnitPrice)
    {
        this.machineUnitPrice = machineUnitPrice;
    }

    public BigDecimal getMachineUnitPrice()
    {
        return machineUnitPrice;
    }

    public void setMinPurchase(Integer minPurchase)
    {
        this.minPurchase = minPurchase;
    }

    public Integer getMinPurchase()
    {
        return minPurchase;
    }

    public void setMaxPurchase(Integer maxPurchase)
    {
        this.maxPurchase = maxPurchase;
    }

    public Integer getMaxPurchase()
    {
        return maxPurchase;
    }

    public void setMinFirstPaymentDays(Integer minFirstPaymentDays)
    {
        this.minFirstPaymentDays = minFirstPaymentDays;
    }

    public Integer getMinFirstPaymentDays()
    {
        return minFirstPaymentDays;
    }

    public void setPlanCycle(Integer planCycle)
    {
        this.planCycle = planCycle;
    }

    public Integer getPlanCycle()
    {
        return planCycle;
    }

    public void setBatchTagType(String batchTagType)
    {
        this.batchTagType = batchTagType;
    }

    public String getBatchTagType()
    {
        return batchTagType;
    }

    public void setMachineCount(Long machineCount)
    {
        this.machineCount = machineCount;
    }

    public Long getMachineCount()
    {
        return machineCount;
    }

    public void setSaleStartTime(Date saleStartTime)
    {
        this.saleStartTime = saleStartTime;
    }

    public Date getSaleStartTime()
    {
        return saleStartTime;
    }

    public void setSaleEndTime(Date saleEndTime)
    {
        this.saleEndTime = saleEndTime;
    }

    public Date getSaleEndTime()
    {
        return saleEndTime;
    }

    public void setExpectedOnlineTime(Date expectedOnlineTime)
    {
        this.expectedOnlineTime = expectedOnlineTime;
    }

    public Date getExpectedOnlineTime()
    {
        return expectedOnlineTime;
    }

    public void setMineLoad(String mineLoad)
    {
        this.mineLoad = mineLoad;
    }

    public String getMineLoad()
    {
        return mineLoad;
    }

    public void setMineLocation(String mineLocation)
    {
        this.mineLocation = mineLocation;
    }

    public String getMineLocation()
    {
        return mineLocation;
    }

    public void setHistoricalOnlineRate(BigDecimal historicalOnlineRate)
    {
        this.historicalOnlineRate = historicalOnlineRate;
    }

    public BigDecimal getHistoricalOnlineRate()
    {
        return historicalOnlineRate;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }

    public void setTagType(String tagType)
    {
        this.tagType = tagType;
    }

    public String getTagType()
    {
        return tagType;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setReservedFieldOne(String reservedFieldOne)
    {
        this.reservedFieldOne = reservedFieldOne;
    }

    public String getReservedFieldOne()
    {
        return reservedFieldOne;
    }

    public void setReservedFieldTwo(String reservedFieldTwo)
    {
        this.reservedFieldTwo = reservedFieldTwo;
    }

    public String getReservedFieldTwo()
    {
        return reservedFieldTwo;
    }

    public void setReservedFieldThree(String reservedFieldThree)
    {
        this.reservedFieldThree = reservedFieldThree;
    }

    public String getReservedFieldThree()
    {
        return reservedFieldThree;
    }

    public void setSingleDayStaticOutputBtc(BigDecimal singleDayStaticOutputBtc)
    {
        this.singleDayStaticOutputBtc = singleDayStaticOutputBtc;
    }

    public BigDecimal getSingleDayStaticOutputBtc()
    {
        return singleDayStaticOutputBtc;
    }

    public void setServiceFeeUnitPrice(BigDecimal serviceFeeUnitPrice)
    {
        this.serviceFeeUnitPrice = serviceFeeUnitPrice;
    }

    public BigDecimal getServiceFeeUnitPrice()
    {
        return serviceFeeUnitPrice;
    }

    public void setDepositDays(Long depositDays)
    {
        this.depositDays = depositDays;
    }

    public Long getDepositDays()
    {
        return depositDays;
    }

    public void setResidualValue(BigDecimal residualValue)
    {
        this.residualValue = residualValue;
    }

    public BigDecimal getResidualValue()
    {
        return residualValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("projectName", getProjectName())
                .append("productImage", getProductImage())
                .append("packageType", getPackageType())
                .append("purchaseLimit", getPurchaseLimit())
                .append("computePowerType", getComputePowerType())
                .append("computePower", getComputePower())
                .append("powerConsumption", getPowerConsumption())
                .append("unitPrice", getUnitPrice())
                .append("unitPowerCost", getUnitPowerCost())
                .append("machineUnitPrice", getMachineUnitPrice())
                .append("minPurchase", getMinPurchase())
                .append("maxPurchase", getMaxPurchase())
                .append("minFirstPaymentDays", getMinFirstPaymentDays())
                .append("planCycle", getPlanCycle())
                .append("batchTagType", getBatchTagType())
                .append("machineCount", getMachineCount())
                .append("saleStartTime", getSaleStartTime())
                .append("saleEndTime", getSaleEndTime())
                .append("expectedOnlineTime", getExpectedOnlineTime())
                .append("mineLoad", getMineLoad())
                .append("mineLocation", getMineLocation())
                .append("historicalOnlineRate", getHistoricalOnlineRate())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("sort", getSort())
                .append("tagType", getTagType())
                .append("type", getType())
                .append("reservedFieldOne", getReservedFieldOne())
                .append("reservedFieldTwo", getReservedFieldTwo())
                .append("reservedFieldThree", getReservedFieldThree())
                .append("singleDayStaticOutputBtc", getSingleDayStaticOutputBtc())
                .append("serviceFeeUnitPrice", getServiceFeeUnitPrice())
                .append("depositDays", getDepositDays())
                .append("residualValue", getResidualValue())
                .toString();
    }
}
