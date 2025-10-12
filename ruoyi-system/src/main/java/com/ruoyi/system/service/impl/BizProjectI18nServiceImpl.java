package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BizProjectI18nMapper;
import com.ruoyi.system.domain.BizProjectI18n;
import com.ruoyi.system.service.IBizProjectI18nService;

/**
 * 项目简介信息Service业务层处理
 *
 * @author Remi
 * @date 2025-09-16
 */
@Service
public class BizProjectI18nServiceImpl implements IBizProjectI18nService
{
    @Autowired
    private BizProjectI18nMapper bizProjectI18nMapper;

    /**
     * 查询项目简介信息
     *
     * @param id 项目简介信息主键
     * @return 项目简介信息
     */
    @Override
    public BizProjectI18n selectBizProjectI18nById(Long id)
    {
        return bizProjectI18nMapper.selectBizProjectI18nById(id);
    }

    /**
     * 查询项目简介信息列表
     *
     * @param bizProjectI18n 项目简介信息
     * @return 项目简介信息
     */
    @Override
    public List<BizProjectI18n> selectBizProjectI18nList(BizProjectI18n bizProjectI18n)
    {
        return bizProjectI18nMapper.selectBizProjectI18nList(bizProjectI18n);
    }

    /**
     * 新增项目简介信息
     *
     * @param bizProjectI18n 项目简介信息
     * @return 结果
     */
    @Override
    public int insertBizProjectI18n(BizProjectI18n bizProjectI18n)
    {
        bizProjectI18n.setCreateTime(DateUtils.getNowDate());
        return bizProjectI18nMapper.insertBizProjectI18n(bizProjectI18n);
    }

    /**
     * 修改项目简介信息
     *
     * @param bizProjectI18n 项目简介信息
     * @return 结果
     */
    @Override
    public int updateBizProjectI18n(BizProjectI18n bizProjectI18n)
    {
        bizProjectI18n.setUpdateTime(DateUtils.getNowDate());
        return bizProjectI18nMapper.updateBizProjectI18n(bizProjectI18n);
    }

    /**
     * 批量删除项目简介信息
     *
     * @param ids 需要删除的项目简介信息主键
     * @return 结果
     */
    @Override
    public int deleteBizProjectI18nByIds(Long[] ids)
    {
        return bizProjectI18nMapper.deleteBizProjectI18nByIds(ids);
    }

    /**
     * 删除项目简介信息信息
     *
     * @param id 项目简介信息主键
     * @return 结果
     */
    @Override
    public int deleteBizProjectI18nById(Long id)
    {
        return bizProjectI18nMapper.deleteBizProjectI18nById(id);
    }
}
