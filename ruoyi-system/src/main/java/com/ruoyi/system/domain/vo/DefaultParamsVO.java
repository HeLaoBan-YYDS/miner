package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(description = "默认参数值VO")
public class DefaultParamsVO {

    @ApiModelProperty(value = "电价 (U)", example = "0.18", notes = "单位：kW*h")
    private BigDecimal electricityPrice;

    @ApiModelProperty(value = "币价 (U)", example = "1115180", notes = "单位：USDT")
    private BigDecimal coinPrice;

    @ApiModelProperty(value = "难度 (T)", example = "113.51", notes = "单位：T")
    private BigDecimal difficulty;

    public BigDecimal getElectricityPrice() {
        return electricityPrice;
    }

    public void setElectricityPrice(BigDecimal electricityPrice) {
        this.electricityPrice = electricityPrice;
    }

    public BigDecimal getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(BigDecimal coinPrice) {
        this.coinPrice = coinPrice;
    }

    public BigDecimal getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(BigDecimal difficulty) {
        this.difficulty = difficulty;
    }
}
