package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

@lombok.Data
@Getter
@Setter
public class BizLogVo {

    private String projectName;

    private BigDecimal orderComputePower;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private BigDecimal incomeAmount;

    private String orderId;
}
