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
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.service.IBizProjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目基本信息Controller
 *
 * @author Remi
 * @date 2025-09-16
 */
@RestController
@RequestMapping("/system/project")
public class BizProjectController extends BaseController
{
    @Autowired
    private IBizProjectService bizProjectService;

    /**
     * 查询项目基本信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizProject bizProject)
    {
        startPage();
        List<BizProject> list = bizProjectService.selectBizProjectList(bizProject);
        return getDataTable(list);
    }

    /**
     * 导出项目基本信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:project:export')")
    @Log(title = "项目基本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizProject bizProject)
    {
        List<BizProject> list = bizProjectService.selectBizProjectList(bizProject);
        ExcelUtil<BizProject> util = new ExcelUtil<BizProject>(BizProject.class);
        util.exportExcel(response, list, "项目基本信息数据");
    }

    /**
     * 获取项目基本信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bizProjectService.selectBizProjectById(id));
    }

    /**
     * 新增项目基本信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:add')")
    @Log(title = "项目基本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizProject bizProject)
    {
        return toAjax(bizProjectService.insertBizProject(bizProject));
    }

    /**
     * 修改项目基本信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:edit')")
    @Log(title = "项目基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizProject bizProject)
    {
        return toAjax(bizProjectService.updateBizProject(bizProject));
    }

    /**
     * 删除项目基本信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:remove')")
    @Log(title = "项目基本信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizProjectService.deleteBizProjectByIds(ids));
    }
}
