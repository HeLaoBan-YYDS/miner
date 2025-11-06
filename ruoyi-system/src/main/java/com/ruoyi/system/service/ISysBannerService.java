package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysBanner;

/**
 * Banner图Service接口
 *
 * @author Remi
 * @date 2025-11-05
 */
public interface ISysBannerService
{
    /**
     * 查询Banner图
     *
     * @param bannerId Banner图主键
     * @return Banner图
     */
    public SysBanner selectSysBannerByBannerId(Long bannerId);

    /**
     * 查询Banner图列表
     *
     * @param sysBanner Banner图
     * @return Banner图集合
     */
    public List<SysBanner> selectSysBannerList(SysBanner sysBanner);

    /**
     * 新增Banner图
     *
     * @param sysBanner Banner图
     * @return 结果
     */
    public int insertSysBanner(SysBanner sysBanner);

    /**
     * 修改Banner图
     *
     * @param sysBanner Banner图
     * @return 结果
     */
    public int updateSysBanner(SysBanner sysBanner);

    /**
     * 批量删除Banner图
     *
     * @param bannerIds 需要删除的Banner图主键集合
     * @return 结果
     */
    public int deleteSysBannerByBannerIds(Long[] bannerIds);

    /**
     * 删除Banner图信息
     *
     * @param bannerId Banner图主键
     * @return 结果
     */
    public int deleteSysBannerByBannerId(Long bannerId);
}
