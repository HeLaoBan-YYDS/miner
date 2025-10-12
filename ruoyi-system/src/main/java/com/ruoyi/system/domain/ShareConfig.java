package com.ruoyi.system.domain;

import lombok.Data;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 分成配置
 */
@Data
public class ShareConfig {

    private Long id;

    /**
     * 电费金额（单位：元）
     * 示例值：0.45
     */
    @DecimalMin(value = "0.00", message = "电费金额不能小于0")
    private BigDecimal electricityFee;

    /**
     * 回本后分成比例（0.3表示30%）
     * 示例值：0.3
     */
    @DecimalMin(value = "0.00", inclusive = false, message = "分成比例必须大于0")
    @DecimalMin(value = "1.00", inclusive = false, message = "分成比例必须小于1")
    private BigDecimal postShare;

    /**
     * 基础分成比例（0.1表示10%）
     * 示例值：0.1
     */
    @DecimalMin(value = "0.00", inclusive = false, message = "分成比例必须大于0")
    @DecimalMin(value = "1.00", inclusive = false, message = "分成比例必须小于1")
    private BigDecimal share;


    /**
     * 计算收益
     * @param computePower 算力
     * @param dailyYieldPerTStr 每T每日收益
     * @param electricityFee 电费是负数单位U
     * @return 收益
     */
    public BigDecimal calIncome(BigDecimal computePower, BigDecimal dailyYieldPerTStr, BigDecimal electricityFee) {

        if (share != null && computePower != null && dailyYieldPerTStr != null) {
            // 计算每日收益
            BigDecimal dailyIncome = computePower.multiply(dailyYieldPerTStr);
            // 扣除电费后的收益
            BigDecimal netIncome = dailyIncome.add(electricityFee);
            if (netIncome.compareTo(BigDecimal.ZERO) <= 0) {
                return BigDecimal.ZERO;
            }
            // 按照分成比例计算用户收益
            BigDecimal userIncome = netIncome.multiply(share);
            return userIncome.setScale(8, RoundingMode.DOWN);
        }


        return null;
    }
}
