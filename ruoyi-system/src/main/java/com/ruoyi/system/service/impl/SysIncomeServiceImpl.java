package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysIncomeMapper;
import com.ruoyi.system.domain.SysIncome;
import com.ruoyi.system.service.ISysIncomeService;

/**
 * 收益管理Service业务层处理
 *
 * @author Remi
 * @date 2025-09-17
 */
@Service
public class SysIncomeServiceImpl implements ISysIncomeService
{
    @Autowired
    private SysIncomeMapper sysIncomeMapper;

    /**
     * 查询收益管理
     *
     * @param incomeId 收益管理主键
     * @return 收益管理
     */
    @Override
    public SysIncome selectSysIncomeByIncomeId(Long incomeId)
    {
        return sysIncomeMapper.selectSysIncomeByIncomeId(incomeId);
    }

    /**
     * 查询收益管理列表
     *
     * @param sysIncome 收益管理
     * @return 收益管理
     */
    @Override
    public List<SysIncome> selectSysIncomeList(SysIncome sysIncome)
    {
        return sysIncomeMapper.selectSysIncomeList(sysIncome);
    }

    /**
     * 新增收益管理
     *
     * @param sysIncome 收益管理
     * @return 结果
     */
    @Override
    public int insertSysIncome(SysIncome sysIncome)
    {
        sysIncome.setCreateTime(DateUtils.getNowDate());
        return sysIncomeMapper.insertSysIncome(sysIncome);
    }

    /**
     * 修改收益管理
     *
     * @param sysIncome 收益管理
     * @return 结果
     */
    @Override
    public int updateSysIncome(SysIncome sysIncome)
    {
        sysIncome.setUpdateTime(DateUtils.getNowDate());
        return sysIncomeMapper.updateSysIncome(sysIncome);
    }

    /**
     * 批量删除收益管理
     *
     * @param incomeIds 需要删除的收益管理主键
     * @return 结果
     */
    @Override
    public int deleteSysIncomeByIncomeIds(Long[] incomeIds)
    {
        return sysIncomeMapper.deleteSysIncomeByIncomeIds(incomeIds);
    }

    /**
     * 删除收益管理信息
     *
     * @param incomeId 收益管理主键
     * @return 结果
     */
    @Override
    public int deleteSysIncomeByIncomeId(Long incomeId)
    {
        return sysIncomeMapper.deleteSysIncomeByIncomeId(incomeId);
    }
}
