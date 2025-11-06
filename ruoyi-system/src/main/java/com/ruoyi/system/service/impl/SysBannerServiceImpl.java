package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysBannerMapper;
import com.ruoyi.system.domain.SysBanner;
import com.ruoyi.system.service.ISysBannerService;

/**
 * Banner图Service业务层处理
 *
 * @author Remi
 * @date 2025-11-05
 */
@Service
public class SysBannerServiceImpl implements ISysBannerService
{
    @Autowired
    private SysBannerMapper sysBannerMapper;

    /**
     * 查询Banner图
     *
     * @param bannerId Banner图主键
     * @return Banner图
     */
    @Override
    public SysBanner selectSysBannerByBannerId(Long bannerId)
    {
        return sysBannerMapper.selectSysBannerByBannerId(bannerId);
    }

    /**
     * 查询Banner图列表
     *
     * @param sysBanner Banner图
     * @return Banner图
     */
    @Override
    public List<SysBanner> selectSysBannerList(SysBanner sysBanner)
    {
        return sysBannerMapper.selectSysBannerList(sysBanner);
    }

    /**
     * 新增Banner图
     *
     * @param sysBanner Banner图
     * @return 结果
     */
    @Override
    public int insertSysBanner(SysBanner sysBanner)
    {
        sysBanner.setCreateTime(DateUtils.getNowDate());
        return sysBannerMapper.insertSysBanner(sysBanner);
    }

    /**
     * 修改Banner图
     *
     * @param sysBanner Banner图
     * @return 结果
     */
    @Override
    public int updateSysBanner(SysBanner sysBanner)
    {
        sysBanner.setUpdateTime(DateUtils.getNowDate());
        return sysBannerMapper.updateSysBanner(sysBanner);
    }

    /**
     * 批量删除Banner图
     *
     * @param bannerIds 需要删除的Banner图主键
     * @return 结果
     */
    @Override
    public int deleteSysBannerByBannerIds(Long[] bannerIds)
    {
        return sysBannerMapper.deleteSysBannerByBannerIds(bannerIds);
    }

    /**
     * 删除Banner图信息
     *
     * @param bannerId Banner图主键
     * @return 结果
     */
    @Override
    public int deleteSysBannerByBannerId(Long bannerId)
    {
        return sysBannerMapper.deleteSysBannerByBannerId(bannerId);
    }
}
