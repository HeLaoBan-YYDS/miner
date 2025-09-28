package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.domain.vo.ProjectVo;
import com.ruoyi.system.service.IBizProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 项目基本信息Controller
 *
 * @author Remi
 * @date 2025-09-16
 */
@RestController
@RequestMapping("/app/project")
@Api(tags = "项目基本信息")
public class AppBizProjectController extends BaseController
{
    @Autowired
    private IBizProjectService bizProjectService;

    /**
     * 查询项目基本信息列表
     */
    @PreAuthorize("@ss.hasRole('user')")
    @GetMapping("/list")
    @ApiOperation("项目列表")
    public TableDataInfo list(BizProject bizProject)
    {
        startPage();
        String currentLang = MessageUtils.getCurrentLang();
        bizProject.getParams().put("langCode", currentLang);
        List<ProjectVo> list = bizProjectService.selectProjectVoList(bizProject);
        return getDataTable(list);
    }




}
