package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysIncome;

/**
 * 收益管理Service接口
 *
 * @author Remi
 * @date 2025-09-17
 */
public interface ISysIncomeService
{
    /**
     * 查询收益管理
     *
     * @param incomeId 收益管理主键
     * @return 收益管理
     */
    public SysIncome selectSysIncomeByIncomeId(Long incomeId);

    /**
     * 查询收益管理列表
     *
     * @param sysIncome 收益管理
     * @return 收益管理集合
     */
    public List<SysIncome> selectSysIncomeList(SysIncome sysIncome);

    /**
     * 新增收益管理
     *
     * @param sysIncome 收益管理
     * @return 结果
     */
    public int insertSysIncome(SysIncome sysIncome);

    /**
     * 修改收益管理
     *
     * @param sysIncome 收益管理
     * @return 结果
     */
    public int updateSysIncome(SysIncome sysIncome);

    /**
     * 批量删除收益管理
     *
     * @param incomeIds 需要删除的收益管理主键集合
     * @return 结果
     */
    public int deleteSysIncomeByIncomeIds(Long[] incomeIds);

    /**
     * 删除收益管理信息
     *
     * @param incomeId 收益管理主键
     * @return 结果
     */
    public int deleteSysIncomeByIncomeId(Long incomeId);
}
