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
import com.ruoyi.system.domain.BizAddress;
import com.ruoyi.system.service.IBizAddressService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 充值地址管理Controller
 *
 * @author Remi
 * @date 2025-09-17
 */
@RestController
@RequestMapping("/system/address")
public class BizAddressController extends BaseController
{
    @Autowired
    private IBizAddressService bizAddressService;

    /**
     * 查询充值地址管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:address:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizAddress bizAddress)
    {
        startPage();
        List<BizAddress> list = bizAddressService.selectBizAddressList(bizAddress);
        return getDataTable(list);
    }

    /**
     * 导出充值地址管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:address:export')")
    @Log(title = "充值地址管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizAddress bizAddress)
    {
        List<BizAddress> list = bizAddressService.selectBizAddressList(bizAddress);
        ExcelUtil<BizAddress> util = new ExcelUtil<BizAddress>(BizAddress.class);
        util.exportExcel(response, list, "充值地址管理数据");
    }

    /**
     * 获取充值地址管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:address:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bizAddressService.selectBizAddressById(id));
    }

    /**
     * 新增充值地址管理
     */
    @PreAuthorize("@ss.hasPermi('system:address:add')")
    @Log(title = "充值地址管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizAddress bizAddress)
    {
        return toAjax(bizAddressService.insertBizAddress(bizAddress));
    }

    /**
     * 修改充值地址管理
     */
    @PreAuthorize("@ss.hasPermi('system:address:edit')")
    @Log(title = "充值地址管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizAddress bizAddress)
    {
        return toAjax(bizAddressService.updateBizAddress(bizAddress));
    }

    /**
     * 删除充值地址管理
     */
    @PreAuthorize("@ss.hasPermi('system:address:remove')")
    @Log(title = "充值地址管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bizAddressService.deleteBizAddressByIds(ids));
    }
}
