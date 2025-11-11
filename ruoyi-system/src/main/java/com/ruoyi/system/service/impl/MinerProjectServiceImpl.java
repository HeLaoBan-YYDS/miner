package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.MinerProjectMapper;
import com.ruoyi.system.domain.MinerProject;
import com.ruoyi.system.service.IMinerProjectService;

/**
 * 项目管理Service业务层处理
 *
 * @author Remi
 * @date 2025-11-10
 */
@Service
public class MinerProjectServiceImpl implements IMinerProjectService
{
    @Autowired
    private MinerProjectMapper minerProjectMapper;

    /**
     * 查询项目管理
     *
     * @param id 项目管理主键
     * @return 项目管理
     */
    @Override
    public MinerProject selectMinerProjectById(Long id)
    {
        return minerProjectMapper.selectMinerProjectById(id);
    }

    /**
     * 查询项目管理列表
     *
     * @param minerProject 项目管理
     * @return 项目管理
     */
    @Override
    public List<MinerProject> selectMinerProjectList(MinerProject minerProject)
    {
        return minerProjectMapper.selectMinerProjectList(minerProject);
    }

    /**
     * 新增项目管理
     *
     * @param minerProject 项目管理
     * @return 结果
     */
    @Override
    public int insertMinerProject(MinerProject minerProject)
    {
        minerProject.setCreateTime(DateUtils.getNowDate());
        return minerProjectMapper.insertMinerProject(minerProject);
    }

    /**
     * 修改项目管理
     *
     * @param minerProject 项目管理
     * @return 结果
     */
    @Override
    public int updateMinerProject(MinerProject minerProject)
    {
        minerProject.setUpdateTime(DateUtils.getNowDate());
        return minerProjectMapper.updateMinerProject(minerProject);
    }

    /**
     * 批量删除项目管理
     *
     * @param ids 需要删除的项目管理主键
     * @return 结果
     */
    @Override
    public int deleteMinerProjectByIds(Long[] ids)
    {
        return minerProjectMapper.deleteMinerProjectByIds(ids);
    }

    /**
     * 删除项目管理信息
     *
     * @param id 项目管理主键
     * @return 结果
     */
    @Override
    public int deleteMinerProjectById(Long id)
    {
        return minerProjectMapper.deleteMinerProjectById(id);
    }
}
