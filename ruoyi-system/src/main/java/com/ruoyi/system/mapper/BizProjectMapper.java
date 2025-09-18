package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.domain.vo.ProjectVo;

/**
 * 项目基本信息Mapper接口
 *
 * @author Remi
 * @date 2025-09-16
 */
public interface BizProjectMapper
{
    /**
     * 查询项目基本信息
     *
     * @param id 项目基本信息主键
     * @return 项目基本信息
     */
    public BizProject selectBizProjectById(Long id);

    /**
     * 查询项目基本信息列表
     *
     * @param bizProject 项目基本信息
     * @return 项目基本信息集合
     */
    public List<BizProject> selectBizProjectList(BizProject bizProject);

    /**
     * 新增项目基本信息
     *
     * @param bizProject 项目基本信息
     * @return 结果
     */
    public int insertBizProject(BizProject bizProject);

    /**
     * 修改项目基本信息
     *
     * @param bizProject 项目基本信息
     * @return 结果
     */
    public int updateBizProject(BizProject bizProject);

    /**
     * 删除项目基本信息
     *
     * @param id 项目基本信息主键
     * @return 结果
     */
    public int deleteBizProjectById(Long id);

    /**
     * 批量删除项目基本信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizProjectByIds(Long[] ids);

    /**
     * 查询项目基本信息列表（包含多语言简介和描述）
     * @param bizProject 过滤条件
     * @return 项目基本信息集合
     */
    List<ProjectVo> selectProjectVoList(BizProject bizProject);
}
