package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.ShareConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
public class MinerInfoVo extends MinerListVo{

    @ApiModelProperty(value = "底部机器单价(T/U)")
    private BigDecimal machineUnitPrice;

    @ApiModelProperty(value = "选择数量(T)", example = "100")
    private int selectedQuantity;

    @ApiModelProperty(value = "托管押金($)", example = "367.2")
    private BigDecimal deposit;

    @ApiModelProperty(value = "机器费($)", example = "6432.2")
    private BigDecimal machineFee;

    @ApiModelProperty(value = "分红模式Id", example = "100")
    private Long shareModeId;

    @ApiModelProperty(value = "服务模式,提供给用户选择")
    private List<ShareConfig> shareModes;

    @ApiModelProperty(value = "首次缴纳天数", example = "10")
    private int firstPaymentDays;

    /**
     * 钱包可抵扣金额
     */
    @ApiModelProperty(value = "钱包可抵扣金额", example = "-100")
    private BigDecimal walletDeductible;

    //========= 回本周期及产出计算
    @ApiModelProperty(value = "参考价格", example = "125000")
    private BigDecimal referencePrice;

    @ApiModelProperty(value = "最低参考价格", example = "")
    private BigDecimal lowestReferencePrice;

    @ApiModelProperty(value = "最高参考价格", example = "500000000")
    private BigDecimal highestReferencePrice;

    @ApiModelProperty(value = "当前静态产出(BTC)", example = "0.00015394")
    private BigDecimal singleDayStaticOutputBtc;

    @ApiModelProperty(value = "矿机残值", example = "4381.92")
    private BigDecimal residualValue;

    @ApiModelProperty(value = "机器数量(T)", example = "100")
    private int minPurchase;

    @ApiModelProperty(value = "托管天数(天)", example = "360")
    private int depositDays;

    @ApiModelProperty(value = "机器投入金额", example = "653722.4")
    private BigDecimal machineInvestment;

    @ApiModelProperty(value = "托管费投入金额", example = "653722.4")
    private BigDecimal depositInvestment;

    @ApiModelProperty(value = "底部静态回本天数", example = "569")
    private BigDecimal bottomStaticReturnDays;

    @ApiModelProperty(value = "预计静态产出($)", example = "852154.56")
    private BigDecimal estimatedStaticOutput;

    @ApiModelProperty(value = "底部静态产出率", example = "130.35")
    private BigDecimal bottomStaticOutputRate;

    @ApiModelProperty(value = "合计", example = "131.25")
    private BigDecimal total;


    /**
     * 机器费 = 机器单价 * 用户选择数量 + 托管押金押金
     * @return 机器费
     */
    public BigDecimal getMachineFee() {
        BigDecimal selectedQuantity = BigDecimal.valueOf(getSelectedQuantity());
        return selectedQuantity.multiply(getMachineUnitPrice()).add(getDeposit());
    }

    /**
     * 机器投入金额 = 机器单价 * 机器数量 + 押金
     * @return 机器投入金额
     */
    public BigDecimal getMachineInvestment() {
        BigDecimal machineCount = BigDecimal.valueOf(getMinPurchase());
        return machineCount.multiply(getMachineUnitPrice()).add(getDepositInvestment());
    }

    public BigDecimal getBottomStaticOutputRate() {
        return getEstimatedStaticOutput().divide(getMachineInvestment(), 2, RoundingMode.HALF_UP);
    }
}
