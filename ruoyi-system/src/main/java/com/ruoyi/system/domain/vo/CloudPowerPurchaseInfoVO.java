package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 云算力购买页面VO
 */
@Data
public class CloudPowerPurchaseInfoVO {

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Long id;

    /**
     * 产品图片URL
     */
    @ApiModelProperty("产品图片URL")
    private String productImage;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String projectName;


    @ApiModelProperty(value = "标签类型(0:新用户专属,1:热销TOP1 2:热销TOP2 3:热销TOP3 11:热销)", example = "1")
    private String tagType;

    /**
     * 矿池
     */
    @ApiModelProperty("矿池")
    private String miningPool = "F2Pool";


    @ApiModelProperty(value = "起售算力(T)", example = "100")
    private int minPurchase;

    /**
     * 计划周期(天)
     */
    @ApiModelProperty(value = "计划周期(天)", example = "300")
    private Integer planCycle;

    /**
     * 静态产出率(%)
     */
    @ApiModelProperty("顶部显示静态产出率(%)")
    private BigDecimal topStaticOutputRate;


    @ApiModelProperty(value = "算力费单价单价(T/Day)", example = "0.01755")
    private BigDecimal unitPrice;

    /**
     * 选择数量(T)
     */
    @ApiModelProperty(value = "选择数量(T)", example = "100")
    private int selectedQuantity;

    @ApiModelProperty(value = "算力费", example = "52.65")
    private BigDecimal computingPowerFee;

    @ApiModelProperty(value = "电费单价(kwh)", example = "0.065")
    private BigDecimal unitPowerCost;

    /**
     * 折合服务费单价(美元/T/天)
     */
    @ApiModelProperty(value = "折合服务费单价(美元/T/Day)", example = "0.0262")
    private BigDecimal serviceFeeUnitPrice;

    /**
     * 首次缴纳天数
     */
    @ApiModelProperty(value = "首次缴纳天数", example = "10")
    private int firstPaymentDays;

    /**
     * 服务费总额
     */
    @ApiModelProperty(value = "服务费", example = "78.6")
    private BigDecimal payServiceFee;


    /**
     * 钱包可抵扣金额
     */
    @ApiModelProperty(value = "钱包可抵扣金额", example = "-100")
    private BigDecimal walletDeductible;


    // ==================== 静态产出分析 ====================
    @ApiModelProperty(value = "参考价格", example = "125000")
    private BigDecimal referencePrice;

    @ApiModelProperty(value = "底部静态产出率", example = "108")
    private BigDecimal bottomStaticOutputRate;

    @ApiModelProperty(value = "最低参考价格", example = "")
    private BigDecimal lowestReferencePrice;

    @ApiModelProperty(value = "最高参考价格", example = "500000000")
    private BigDecimal highestReferencePrice;

    @ApiModelProperty(value = "期初投入", example = "67.2")
    private BigDecimal initialInvestment;

    @ApiModelProperty(value = "期中投入", example = "77.4")
    private BigDecimal middleInvestment;

    @ApiModelProperty(value = "投入", example = "144.6")
    private BigDecimal totalInvestment;

    @ApiModelProperty(value = "周期静态产出(BTC)", example = "0.00129")
    private BigDecimal staticOutputBTC;

    @ApiModelProperty(value = "周期静态产出($)", example = "161.25")
    private BigDecimal staticOutputUSD;

    @ApiModelProperty(value = "挖矿净得($)", example = "83.5")
    private BigDecimal netProfit;

    // ==================== 静态回本天数 ====================
    @ApiModelProperty(value = "静态回本天数", example = "25")
    private BigDecimal staticReturnDays;

    /**
     * 期中投入 = 总服务费 - 期初服务费
     */
    public void setMiddleInvestment(BigDecimal totalServiceFee) {
      this.middleInvestment = totalServiceFee.subtract(getPayServiceFee());
    }

    /**
     * 周期静态产出(BTC) = 单日静态产出(BTC) * 周期
     */
    public void setStaticOutputBTC(BigDecimal singleDayStaticOutputBTC) {
        this.staticOutputBTC = BigDecimal.valueOf(getPlanCycle()).multiply(singleDayStaticOutputBTC);
    }

    public BigDecimal getStaticOutputUSD() {
        return getStaticOutputBTC().multiply(getReferencePrice());
    }

    /**
     * 总投入 = 期初投入 + 期中投入
     * @return 总投入
     */
    public BigDecimal getTotalInvestment() {
        return getInitialInvestment().add(getMiddleInvestment());
    }

    /**
     * 底部静态产出率
     */
    public BigDecimal getBottomStaticOutputRate() {
        return  getNetProfit().divide(getInitialInvestment(), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    /**
     * 期初投入 = 服务费 + 算力费
     */
    public BigDecimal getInitialInvestment() {
        return getPayServiceFee().add(getComputingPowerFee());
    }

    /**
     * 支付服务费 = 服务费单价 * 选择数量 * 首次缴纳天数
     * @return 支付服务费
     */
    public BigDecimal getPayServiceFee() {
        return BigDecimal.valueOf(firstPaymentDays).multiply(BigDecimal.valueOf(selectedQuantity)).multiply(serviceFeeUnitPrice);
    }

    /**
     * 挖矿净得 = 周期静态产出 - 服务费
     */
    public BigDecimal getNetProfit() {
        return getStaticOutputUSD().subtract(getMiddleInvestment());
    }

    /**
     * 静态回本天数 = 期初投入 / 挖矿净得 * 周期
     */
    public BigDecimal getStaticReturnDays() {
        return getInitialInvestment().divide(getNetProfit(), 4, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(getPlanCycle()));
    }
}
