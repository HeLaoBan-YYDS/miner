package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(description = "矿机收益计算参数DTO")
public class MiningProfitDTO {

    @ApiModelProperty(value = "电价 (U)", example = "0.18", notes = "单位：kW*h", required = false)
    private BigDecimal electricityPrice;

    @ApiModelProperty(value = "币价 (U)", example = "1115180", notes = "单位：USDT", required = false)
    private BigDecimal coinPrice;

    @ApiModelProperty(value = "难度 (T)", example = "113.51", notes = "单位：T", required = false)
    private BigDecimal difficulty;

    @ApiModelProperty(value = "搜索关键词", example = "比特币", required = false)
    private String searchKeyword;

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

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}
