package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 充值地址管理对象 biz_address
 *
 * @author Remi
 * @date 2025-09-17
 */
public class BizAddress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** sys_user_id */
    @Excel(name = "sys_user_id")
    private Long userId;

    /** 使用标志 1使用 0 未使用 */
    @Excel(name = "使用标志 1使用 0 未使用")
    private String useFlag;

    /** 币种 */
    @Excel(name = "币种")
    private String coinType;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUseFlag(String useFlag)
    {
        this.useFlag = useFlag;
    }

    public String getUseFlag()
    {
        return useFlag;
    }

    public void setCoinType(String coinType)
    {
        this.coinType = coinType;
    }

    public String getCoinType()
    {
        return coinType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("address", getAddress())
            .append("userId", getUserId())
            .append("useFlag", getUseFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("coinType", getCoinType())
            .toString();
    }
}
