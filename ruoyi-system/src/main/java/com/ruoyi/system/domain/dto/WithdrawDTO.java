package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Getter
@Setter
public class WithdrawDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    @ApiModelProperty(value = "提现金额(要注意金额合法性)", required = true)
    private BigDecimal amount;


    @NotBlank(message = "Address cannot be empty")
    @ApiModelProperty(value = "提现地址(要做格式校验)", required = true)
    private String address;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id(不用传)")
    private Long userId;

    /**
     * google验证码
     */
    @ApiModelProperty(value = "GoogleCode(不用传)")
    private Long googleCode;


    @NotBlank(message = "{coin.notblank}")
    @ApiModelProperty(value = "币种(usdt_trc20,btc)", required = true)
    private String coin;
}
