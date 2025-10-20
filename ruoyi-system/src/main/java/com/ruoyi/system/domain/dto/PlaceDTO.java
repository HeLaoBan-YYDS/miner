package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Getter
@Setter
public class PlaceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项目id", required = true)
    private Long projectId;

    @DecimalMin(value = "1")
    @ApiModelProperty(value = "算力(必须大于1而且是整数)", required = true)
    private BigDecimal computePower;

    @ApiModelProperty(value = "用户id(不用传)")
    private Long userId;

    /**
     * 前端传入的价格
     */
    @ApiModelProperty(value = "支付金额(单位USDT),注意交易金额合法性",required = true)
    private BigDecimal paymentAmount;

    /**
     *收益模式
     */
    @ApiModelProperty(value = "收益模式id",required = true)
    private Long shareModeId;
}
