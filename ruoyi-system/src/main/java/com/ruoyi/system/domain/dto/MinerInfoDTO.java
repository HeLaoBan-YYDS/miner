package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MinerInfoDTO extends CloudInfoDTO {

    @ApiModelProperty(value = "分成模式Id", example = "1")
    private Long shareModeId;

    @ApiModelProperty(value = "机器数量(台)")
    private Integer machineCount;

    @ApiModelProperty(value = "托管天数")
    private Integer depositDays;

    @ApiModelProperty(value = "当前静态产出(BTC)", example = "0.00015394")
    private BigDecimal unitStaticOutputBTC;
}
