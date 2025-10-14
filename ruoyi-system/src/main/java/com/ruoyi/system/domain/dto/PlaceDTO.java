package com.ruoyi.system.domain.dto;

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

    private Long projectId;

    @DecimalMin(value = "1")
    private BigDecimal computePower;

    private Long userId;

    /**
     * 前端传入的价格
     */
    private BigDecimal paymentAmount;

    /**
     *收益模式
     */
    private Long shareModeId;
}
