package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel(description = "矿池基础信息VO")
public class PoolInfoVO {

    @ApiModelProperty(value = "总币数(流通量)")
    private BigDecimal circulationBTC;

    @ApiModelProperty(value = "全网算力")
    private String hashrate24h;

    @ApiModelProperty(value = "总出块数")
    private Long blocks;

    @ApiModelProperty(value = "孤块数")
    private Integer orphanBlocks;

    @ApiModelProperty(value = "孤块率")
    private BigDecimal orphanRate;

    public BigDecimal getOrphanRate() {
        return orphanRate;
    }

    public void setOrphanRate(BigDecimal orphanRate) {
        this.orphanRate = orphanRate;
    }

    public Integer getOrphanBlocks() {
        return orphanBlocks;
    }

    public void setOrphanBlocks(Integer orphanBlocks) {
        this.orphanBlocks = orphanBlocks;
    }


    public BigDecimal getCirculationBTC() {
        return circulationBTC;
    }

    public void setCirculationBTC(BigDecimal circulationBTC) {
        this.circulationBTC = circulationBTC;
    }

    public String getHashrate24h() {
        return hashrate24h;
    }

    public void setHashrate24h(String hashrate24h) {
        this.hashrate24h = hashrate24h;
    }

    public Long getBlocks() {
        return blocks;
    }

    public void setBlocks(Long blocks) {
        this.blocks = blocks;
    }
}
