package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("APR信息VO")
@Data
public class AprInfoVo {
    @ApiModelProperty("每日单T产出")
    private String dailyYieldPerT;

    @ApiModelProperty("电费")
    private String dailyPowerFee;

    @ApiModelProperty("平台算力")
    private String platformTotalComputePower;
}
