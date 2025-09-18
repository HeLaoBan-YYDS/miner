package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysIncome;
import com.ruoyi.system.service.ISysIncomeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 收益管理Controller
 *
 * @author Remi
 * @date 2025-09-17
 */
@RestController
@RequestMapping("/system/income")
public class SysIncomeController extends BaseController
{
    @Autowired
    private ISysIncomeService sysIncomeService;

    /**
     * 查询收益管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:income:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysIncome sysIncome)
    {
        startPage();
        List<SysIncome> list = sysIncomeService.selectSysIncomeList(sysIncome);
        return getDataTable(list);
    }

    /**
     * 导出收益管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:income:export')")
    @Log(title = "收益管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysIncome sysIncome)
    {
        List<SysIncome> list = sysIncomeService.selectSysIncomeList(sysIncome);
        ExcelUtil<SysIncome> util = new ExcelUtil<SysIncome>(SysIncome.class);
        util.exportExcel(response, list, "收益管理数据");
    }

    /**
     * 获取收益管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:income:query')")
    @GetMapping(value = "/{incomeId}")
    public AjaxResult getInfo(@PathVariable("incomeId") Long incomeId)
    {
        return success(sysIncomeService.selectSysIncomeByIncomeId(incomeId));
    }

    /**
     * 新增收益管理
     */
    @PreAuthorize("@ss.hasPermi('system:income:add')")
    @Log(title = "收益管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysIncome sysIncome)
    {
        return toAjax(sysIncomeService.insertSysIncome(sysIncome));
    }

    /**
     * 修改收益管理
     */
    @PreAuthorize("@ss.hasPermi('system:income:edit')")
    @Log(title = "收益管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysIncome sysIncome)
    {
        return toAjax(sysIncomeService.updateSysIncome(sysIncome));
    }

    /**
     * 删除收益管理
     */
    @PreAuthorize("@ss.hasPermi('system:income:remove')")
    @Log(title = "收益管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{incomeIds}")
    public AjaxResult remove(@PathVariable Long[] incomeIds)
    {
        return toAjax(sysIncomeService.deleteSysIncomeByIncomeIds(incomeIds));
    }
}
