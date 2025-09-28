package com.ruoyi.system.domain.vo;

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

    private Date createTime;

    private BigDecimal incomeAmount;
}
