package com.ruoyi.system.domain.dto;

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
    private BigDecimal amount;


    @NotBlank(message = "Address cannot be empty")
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
