package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.utils.MessageUtils;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CloudPowerListVo {
    /** 项目ID */
    private Long id;

    /** 项目名称 */
    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty(value = "套餐类型")
    private String packageType;

    @ApiModelProperty("购买限制 1:限购一次 2:可复购")
    private String purchaseLimit;

    @ApiModelProperty(value = "标签类型(0:新用户专属,1:热销TOP1 2:热销TOP2 3:热销TOP3 11热销)")
    private String tagType;

    @ApiModelProperty("最低购买(T),也有可能是固定购买看purchaseLimit类型")
    private int minPurchase;

    @ApiModelProperty("计划周期")
    private int planCycle;

    @ApiModelProperty("静态产出率")
    private BigDecimal staticOutputRate;


    public String getPurchaseLimit() {
        if (purchaseLimit == null) return null;
        return MessageUtils.message(purchaseLimit);
    }
}
