package com.ruoyi.system.service.impl;

import java.util.Collections;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.system.domain.vo.ProjectVo;
import com.ruoyi.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BizProjectMapper;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.service.IBizProjectService;

/**
 * 项目基本信息Service业务层处理
 *
 * @author Remi
 * @date 2025-09-16
 */
@Service
public class BizProjectServiceImpl implements IBizProjectService
{
    @Autowired
    private BizProjectMapper bizProjectMapper;

    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询项目基本信息
     *
     * @param id 项目基本信息主键
     * @return 项目基本信息
     */
    @Override
    public BizProject selectBizProjectById(Long id)
    {
        return bizProjectMapper.selectBizProjectById(id);
    }

    /**
     * 查询项目基本信息列表
     *
     * @param bizProject 项目基本信息
     * @return 项目基本信息
     */
    @Override
    public List<BizProject> selectBizProjectList(BizProject bizProject)
    {
        return bizProjectMapper.selectBizProjectList(bizProject);
    }

    /**
     * 新增项目基本信息
     *
     * @param bizProject 项目基本信息
     * @return 结果
     */
    @Override
    public int insertBizProject(BizProject bizProject)
    {
        bizProject.setCreateTime(DateUtils.getNowDate());

        int row = bizProjectMapper.insertBizProject(bizProject);

        if (row >0){
            // 添加字典数据
            SysDictData dictData = new SysDictData();
            dictData.setDictType("project_dict");
            dictData.setDictLabel(bizProject.getProjectName()); // 项目名称作为标签
            dictData.setDictValue(bizProject.getId().toString()); // 项目ID作为值
            dictDataService.insertDictData(dictData);
        }


        return row;
    }

    /**
     * 修改项目基本信息
     *
     * @param bizProject 项目基本信息
     * @return 结果
     */
    @Override
    public int updateBizProject(BizProject bizProject)
    {
        bizProject.setUpdateTime(DateUtils.getNowDate());
        dictDataService.updateProjectTypeDict(bizProject.getId().toString(), bizProject.getProjectName());
        return bizProjectMapper.updateBizProject(bizProject);
    }

    /**
     * 批量删除项目基本信息
     *
     * @param ids 需要删除的项目基本信息主键
     * @return 结果
     */
    @Override
    public int deleteBizProjectByIds(Long[] ids)
    {
        return bizProjectMapper.deleteBizProjectByIds(ids);
    }

    /**
     * 删除项目基本信息信息
     *
     * @param id 项目基本信息主键
     * @return 结果
     */
    @Override
    public int deleteBizProjectById(Long id)
    {
        return bizProjectMapper.deleteBizProjectById(id);
    }

    @Override
    public List<ProjectVo> selectProjectVoList(BizProject bizProject) {
        return bizProjectMapper.selectProjectVoList(bizProject);
    }
}
