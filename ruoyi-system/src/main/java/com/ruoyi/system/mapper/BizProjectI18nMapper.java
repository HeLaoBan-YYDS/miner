package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizProjectI18n;

/**
 * 项目简介信息Mapper接口
 *
 * @author Remi
 * @date 2025-09-16
 */
public interface BizProjectI18nMapper
{
    /**
     * 查询项目简介信息
     *
     * @param id 项目简介信息主键
     * @return 项目简介信息
     */
    public BizProjectI18n selectBizProjectI18nById(Long id);

    /**
     * 查询项目简介信息列表
     *
     * @param bizProjectI18n 项目简介信息
     * @return 项目简介信息集合
     */
    public List<BizProjectI18n> selectBizProjectI18nList(BizProjectI18n bizProjectI18n);

    /**
     * 新增项目简介信息
     *
     * @param bizProjectI18n 项目简介信息
     * @return 结果
     */
    public int insertBizProjectI18n(BizProjectI18n bizProjectI18n);

    /**
     * 修改项目简介信息
     *
     * @param bizProjectI18n 项目简介信息
     * @return 结果
     */
    public int updateBizProjectI18n(BizProjectI18n bizProjectI18n);

    /**
     * 删除项目简介信息
     *
     * @param id 项目简介信息主键
     * @return 结果
     */
    public int deleteBizProjectI18nById(Long id);

    /**
     * 批量删除项目简介信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizProjectI18nByIds(Long[] ids);
}
