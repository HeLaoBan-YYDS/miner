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
import com.ruoyi.system.domain.BizLog;
import com.ruoyi.system.service.IBizLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 资金流水Controller
 *
 * @author Remi
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/system/log")
public class BizLogController extends BaseController
{
    @Autowired
    private IBizLogService bizLogService;

    /**
     * 查询资金流水列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizLog bizLog)
    {
        startPage();
        List<BizLog> list = bizLogService.selectBizLogList(bizLog);
        return getDataTable(list);
    }

    /**
     * 导出资金流水列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:export')")
    @Log(title = "资金流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizLog bizLog)
    {
        List<BizLog> list = bizLogService.selectBizLogList(bizLog);
        ExcelUtil<BizLog> util = new ExcelUtil<BizLog>(BizLog.class);
        util.exportExcel(response, list, "资金流水数据");
    }

    /**
     * 获取资金流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:log:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return success(bizLogService.selectBizLogByLogId(logId));
    }

    /**
     * 新增资金流水
     */
    @PreAuthorize("@ss.hasPermi('system:log:add')")
    @Log(title = "资金流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizLog bizLog)
    {
        return toAjax(bizLogService.insertBizLog(bizLog));
    }

    /**
     * 修改资金流水
     */
    @PreAuthorize("@ss.hasPermi('system:log:edit')")
    @Log(title = "资金流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizLog bizLog)
    {
        return toAjax(bizLogService.updateBizLog(bizLog));
    }

    /**
     * 删除资金流水
     */
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "资金流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(bizLogService.deleteBizLogByLogIds(logIds));
    }
}
