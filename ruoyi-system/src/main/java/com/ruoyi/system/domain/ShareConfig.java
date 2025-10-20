package com.ruoyi.system.domain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * 分成配置
 */
@Data
public class ShareConfig {

    @ApiModelProperty("id")
    private Long id;

    /**
     * 电费金额（单位：元）
     * 示例值：0.45
     */
    @DecimalMin(value = "0.00", message = "电费金额不能小于0")
    @ApiModelProperty("电费金额")
    private BigDecimal electricityFee;

    /**
     * 回本后分成比例（0.3表示30%）
     * 示例值：0.3
     */
    @DecimalMin(value = "0.00", inclusive = false, message = "分成比例必须大于0")
    @DecimalMin(value = "1.00", inclusive = false, message = "分成比例必须小于1")
    @ApiModelProperty("分成比例")
    private BigDecimal postShare;

    /**
     * 基础分成比例（0.1表示10%）
     * 示例值：0.1
     */
    @DecimalMin(value = "0.00", inclusive = false, message = "分成比例必须大于0")
    @DecimalMin(value = "1.00", inclusive = false, message = "分成比例必须小于1")
    @ApiModelProperty("基础分成比例")
    private BigDecimal share;


    /**
     * 计算用户能获得的收益 (BTC)
     *
     * @param dailyIncome    每日收益（BTC）
     * @param electricityFee 电费是负数单位BTC
     * @param isReturn       是否回本后
     * @return 收益（BTC）
     */
    public IncomeInfo calUserIncome(BigDecimal dailyIncome, BigDecimal electricityFee, Boolean isReturn) {
        BigDecimal profit = dailyIncome.add(electricityFee);

        BigDecimal commission = calCommission(profit, isReturn);

        BigDecimal userIncome = dailyIncome.subtract(commission);

        return new IncomeInfo(userIncome,commission,dailyIncome);
    }

    /**
     * 计算平台抽成 (BTC)
     *
     * @param profit   利润（BTC）
     * @param isReturn 是否回本
     * @return 平台抽成（BTC）
     */
    public BigDecimal calCommission(BigDecimal profit, Boolean isReturn) {
        if (profit == null || profit.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        if (share != null) {
            return profit.multiply(share);
        }

        if (postShare != null && isReturn) {
            return profit.multiply(postShare);
        }
        return BigDecimal.ZERO;
    }
}
