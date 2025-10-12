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
import com.ruoyi.system.domain.BizProjectI18n;
import com.ruoyi.system.service.IBizProjectI18nService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目简介信息Controller
 *
 * @author Remi
 * @date 2025-09-16
 */
@RestController
@RequestMapping("/system/i18n")
public class BizProjectI18nController extends BaseController
{
    @Autowired
    private IBizProjectI18nService bizProjectI18nService;

    /**
     * 查询项目简介信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:i18n:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizProjectI18n bizProjectI18n)
    {
        startPage();
        List<BizProjectI18n> list = bizProjectI18nService.selectBizProjectI18nList(bizProjectI18n);
        return getDataTable(list);
    }

    /**
     * 导出项目简介信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:i18n:export')")
    @Log(title = "项目简介信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizProjectI18n bizProjectI18n)
    {
        List<BizProjectI18n> list = bizProjectI18nService.selectBizProjectI18nList(bizProjectI18n);
        ExcelUtil<BizProjectI18n> util = new ExcelUtil<BizProjectI18n>(BizProjectI18n.class);
        util.exportExcel(response, list, "项目简介信息数据");
    }

    /**
     * 获取项目简介信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:i18n:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bizProjectI18nService.selectBizProjectI18nById(id));
    }

    /**
     * 新增项目简介信息
     */
    @PreAuthorize("@ss.hasPermi('system:i18n:add')")
    @Log(title = "项目简介信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizProjectI18n bizProjectI18n)
    {
        return toAjax(bizProjectI18nService.insertBizProjectI18n(bizProjectI18n));
    }

    /**
     * 修改项目简介信息
     */
    @PreAuthorize("@ss.hasPermi('system:i18n:edit')")
    @Log(title = "项目简介信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizProjectI18n bizProjectI18n)
    {
        return toAjax(bizProjectI18nService.updateBizProjectI18n(bizProjectI18n));
    }

    /**
     * 删除项目简介信息
     */
    @PreAuthorize("@ss.hasPermi('system:i18n:remove')")
    @Log(title = "项目简介信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizProjectI18nService.deleteBizProjectI18nByIds(ids));
    }
}
