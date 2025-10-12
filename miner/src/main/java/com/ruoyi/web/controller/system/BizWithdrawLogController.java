package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.LogType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizLog;
import com.ruoyi.system.service.IBizLogService;
import com.ruoyi.system.service.IBizWithdrawService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 资金流水Controller
 *
 * @author Remi
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/system/withdraw")
public class BizWithdrawLogController extends BaseController
{
    @Autowired
    private IBizWithdrawService bizWithdrawService;

    @Autowired
    private IBizLogService bizLogService;

    /**
     * 查看提现记录
     */
    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/log/list")
    public TableDataInfo list(BizLog bizLog)
    {
        startPage();
        bizLog.setLogType(LogType.WITHDRAW.getCode());
        List<BizLog> list = bizLogService.selectBizLogList(bizLog);
        for (BizLog log : list) {
            log.setAmount(log.getAmount().negate());
        }
        return getDataTable(list);
    }

    /**
     * 导出资金流水列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:export')")
    @Log(title = "资金流水", businessType = BusinessType.EXPORT)
    @PostMapping("/log/export")
    public void export(HttpServletResponse response, BizLog bizLog)
    {
        bizLog.setLogType(LogType.WITHDRAW.getCode());
        List<BizLog> list = bizLogService.selectBizLogList(bizLog);
        ExcelUtil<BizLog> util = new ExcelUtil<BizLog>(BizLog.class);
        util.exportExcel(response, list, "资金流水数据");
    }

    /**
     * 提现审核通过
     * @param id 提现记录ID
     * @return  AjaxResult
     */
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "提现审核通过", businessType = BusinessType.DELETE)
    @GetMapping("/pass/{id}")
    @RepeatSubmit
    @ApiOperation("提现审核通过")
    public AjaxResult handlePass(@PathVariable("id") Long id)
    {
        bizWithdrawService.handlePass(id);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "提现审核不通过", businessType = BusinessType.DELETE)
    @GetMapping("/noPass/{id}")
    @RepeatSubmit
    @ApiOperation("提现审核不通过")
    public AjaxResult handleNoPass(@PathVariable("id") Long id)
    {
        bizWithdrawService.handleNoPass(id);
        return success();
    }

}
