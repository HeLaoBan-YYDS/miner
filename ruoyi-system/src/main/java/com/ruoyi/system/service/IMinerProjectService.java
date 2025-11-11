package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.MinerProject;

/**
 * 项目管理Service接口
 *
 * @author Remi
 * @date 2025-11-10
 */
public interface IMinerProjectService
{
    /**
     * 查询项目管理
     *
     * @param id 项目管理主键
     * @return 项目管理
     */
    public MinerProject selectMinerProjectById(Long id);

    /**
     * 查询项目管理列表
     *
     * @param minerProject 项目管理
     * @return 项目管理集合
     */
    public List<MinerProject> selectMinerProjectList(MinerProject minerProject);

    /**
     * 新增项目管理
     *
     * @param minerProject 项目管理
     * @return 结果
     */
    public int insertMinerProject(MinerProject minerProject);

    /**
     * 修改项目管理
     *
     * @param minerProject 项目管理
     * @return 结果
     */
    public int updateMinerProject(MinerProject minerProject);

    /**
     * 批量删除项目管理
     *
     * @param ids 需要删除的项目管理主键集合
     * @return 结果
     */
    public int deleteMinerProjectByIds(Long[] ids);

    /**
     * 删除项目管理信息
     *
     * @param id 项目管理主键
     * @return 结果
     */
    public int deleteMinerProjectById(Long id);
}
