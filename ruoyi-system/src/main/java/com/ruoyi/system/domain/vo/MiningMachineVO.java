package com.ruoyi.system.domain.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 矿机信息VO
 *
 * @author Remi
 * @date 2025-09-19
 */
@Data
public class MiningMachineVO {

    /**
     * 矿机型号
     */
    private String projectName;

    /**
     * 可挖币种
     */
    private String currency = "BTC";

    /**
     * 电费(U)
     */
    private BigDecimal electricityFee;

    /**
     * 电费占比
     */
    private BigDecimal electricityRatio;

    /**
     * 日净利润(U)
     */
    private BigDecimal dailyNetProfit;

    /**
     * 关机币价(U)
     */
    private BigDecimal shutdownPrice;
}
