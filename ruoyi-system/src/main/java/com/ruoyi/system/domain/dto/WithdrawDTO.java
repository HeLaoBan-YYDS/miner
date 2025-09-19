package com.ruoyi.system.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@Getter
@Setter
public class WithdrawDTO {

    @DecimalMin(value = "0.01", message = "{amount.min}")
    @NotBlank(message = "{amount.notblank}")
    private BigDecimal amount;


    @NotBlank(message = "{address.notblank}")
    private String address;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * google验证码
     */
    private Long googleCode;

    @NotBlank(message = "{coin.notblank}")
    private String coin;
}
