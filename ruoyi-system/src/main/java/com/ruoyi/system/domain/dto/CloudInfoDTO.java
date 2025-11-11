package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CloudInfoDTO {

    @ApiModelProperty(value = "项目id",required = true)
    private Long id;

    @ApiModelProperty("选择数量T")
    private Integer selectedQuantity;

    @ApiModelProperty(value = "首次缴纳天数", example = "10")
    private Integer firstPaymentDays;

    @ApiModelProperty(value = "参考价格", example = "125000")
    private BigDecimal referencePrice;
}
