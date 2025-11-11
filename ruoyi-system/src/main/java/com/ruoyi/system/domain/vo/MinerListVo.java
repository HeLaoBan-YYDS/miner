package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.MessageUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MinerListVo {

    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "销售标签")
    private String tagType;

    @ApiModelProperty(value = "批次标签 1:第一期1批")
    private String batchTagType;

    @ApiModelProperty(value = "开售时间:YYYY/MM/DD")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date saleStartTime;

    @ApiModelProperty(value = "机器单价(/T)")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "托管周期(天)")
    private int planCycle;

    @ApiModelProperty("静态回本天数(列表购买页上方显示)")
    private BigDecimal topStaticReturnDays;


    public String getBatchTagType() {
        if (batchTagType == null) return null;
        return MessageUtils.message(batchTagType);
    }
}
