package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("幸运值信息VO")
@Data
public class LuckValueVO {
    @ApiModelProperty("3天幸运值")
    private double threeDay;

    @ApiModelProperty("7天幸运值")
    private double sevenDay;

}
