package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Banner图对象 sys_banner
 *
 * @author Remi
 * @date 2025-11-05
 */
public class SysBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** BannerID */
    private Long bannerId;

    /** Banner标题 */
    @Excel(name = "Banner标题")
    @ApiModelProperty("Banner标题")
    private String bannerTitle;

    /** Banner图片地址 */
    @Excel(name = "Banner图片地址")
    @ApiModelProperty("Banner图片地址")
    private String bannerImage;

    /** Banner跳转链接 */
    @Excel(name = "Banner跳转链接")
    @ApiModelProperty("Banner跳转链接")
    private String bannerUrl;

    /** Banner位置(HOME-首页等) */
    @Excel(name = "Banner位置(HOME-首页等)")
    @ApiModelProperty("Banner位置(HOME-首页等)")
    private String bannerPosition;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    @ApiModelProperty("显示顺序")
    private Long sortOrder;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 多语言 */
    @Excel(name = "多语言")
    @ApiModelProperty("多语言Code")
    private String language;

    public void setBannerId(Long bannerId)
    {
        this.bannerId = bannerId;
    }

    public Long getBannerId()
    {
        return bannerId;
    }

    public void setBannerTitle(String bannerTitle)
    {
        this.bannerTitle = bannerTitle;
    }

    public String getBannerTitle()
    {
        return bannerTitle;
    }

    public void setBannerImage(String bannerImage)
    {
        this.bannerImage = bannerImage;
    }

    public String getBannerImage()
    {
        return bannerImage;
    }

    public void setBannerUrl(String bannerUrl)
    {
        this.bannerUrl = bannerUrl;
    }

    public String getBannerUrl()
    {
        return bannerUrl;
    }

    public void setBannerPosition(String bannerPosition)
    {
        this.bannerPosition = bannerPosition;
    }

    public String getBannerPosition()
    {
        return bannerPosition;
    }

    public void setSortOrder(Long sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder()
    {
        return sortOrder;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getLanguage()
    {
        return language;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("bannerId", getBannerId())
            .append("bannerTitle", getBannerTitle())
            .append("bannerImage", getBannerImage())
            .append("bannerUrl", getBannerUrl())
            .append("bannerPosition", getBannerPosition())
            .append("sortOrder", getSortOrder())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .append("language", getLanguage())
            .toString();
    }
}
