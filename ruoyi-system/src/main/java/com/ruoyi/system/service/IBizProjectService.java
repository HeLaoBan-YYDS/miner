package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.domain.vo.ProjectVo;

/**
 * 项目基本信息Service接口
 *
 * @author Remi
 * @date 2025-09-16
 */
public interface IBizProjectService
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
     * 批量删除项目基本信息
     *
     * @param ids 需要删除的项目基本信息主键集合
     * @return 结果
     */
    public int deleteBizProjectByIds(Long[] ids);

    /**
     * 删除项目基本信息信息
     *
     * @param id 项目基本信息主键
     * @return 结果
     */
    public int deleteBizProjectById(Long id);

    /**
     * 查询项目基本信息列表（App端专用）
     *
     * @return 项目基本信息集合
     */
    public List<ProjectVo> selectProjectVoList(BizProject bizProject);
}
