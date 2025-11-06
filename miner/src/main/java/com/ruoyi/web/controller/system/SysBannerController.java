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
import com.ruoyi.system.domain.SysBanner;
import com.ruoyi.system.service.ISysBannerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * Banner图Controller
 *
 * @author Remi
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/system/banner")
public class SysBannerController extends BaseController
{
    @Autowired
    private ISysBannerService sysBannerService;

    /**
     * 查询Banner图列表
     */
    @PreAuthorize("@ss.hasPermi('system:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysBanner sysBanner)
    {
        startPage();
        List<SysBanner> list = sysBannerService.selectSysBannerList(sysBanner);
        return getDataTable(list);
    }

    /**
     * 导出Banner图列表
     */
    @PreAuthorize("@ss.hasPermi('system:banner:export')")
    @Log(title = "Banner图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysBanner sysBanner)
    {
        List<SysBanner> list = sysBannerService.selectSysBannerList(sysBanner);
        ExcelUtil<SysBanner> util = new ExcelUtil<SysBanner>(SysBanner.class);
        util.exportExcel(response, list, "Banner图数据");
    }

    /**
     * 获取Banner图详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:banner:query')")
    @GetMapping(value = "/{bannerId}")
    public AjaxResult getInfo(@PathVariable("bannerId") Long bannerId)
    {
        return success(sysBannerService.selectSysBannerByBannerId(bannerId));
    }

    /**
     * 新增Banner图
     */
    @PreAuthorize("@ss.hasPermi('system:banner:add')")
    @Log(title = "Banner图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysBanner sysBanner)
    {
        return toAjax(sysBannerService.insertSysBanner(sysBanner));
    }

    /**
     * 修改Banner图
     */
    @PreAuthorize("@ss.hasPermi('system:banner:edit')")
    @Log(title = "Banner图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysBanner sysBanner)
    {
        return toAjax(sysBannerService.updateSysBanner(sysBanner));
    }

    /**
     * 删除Banner图
     */
    @PreAuthorize("@ss.hasPermi('system:banner:remove')")
    @Log(title = "Banner图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bannerIds}")
    public AjaxResult remove(@PathVariable Long[] bannerIds)
    {
        return toAjax(sysBannerService.deleteSysBannerByBannerIds(bannerIds));
    }
}
