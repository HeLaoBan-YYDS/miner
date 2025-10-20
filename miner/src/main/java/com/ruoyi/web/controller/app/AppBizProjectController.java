package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.LogType;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.domain.vo.ProjectVo;
import com.ruoyi.system.service.IBizProjectService;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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

    @Autowired
    private ISysConfigService configService;

    /**
     * 查询项目基本信息列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "项目列表",response = ProjectVo.class)
    @Anonymous
    public TableDataInfo list(BizProject bizProject)
    {
        startPage();
        String currentLang = MessageUtils.getCurrentLang();
        bizProject.getParams().put("langCode", currentLang);
        bizProject.setStatus(ProjectStatus.SUCCESS.getCode());
        List<ProjectVo> list = bizProjectService.selectProjectVoList(bizProject);
        return getDataTable(list);
    }

    /**
     * 查询项目基本信息列表
     */
    @GetMapping("/apr")
    @ApiOperation("APR")
    @Anonymous
    public AjaxResult apr()
    {
        //每日单T产出
        String dailyYieldPerT = configService.selectConfigByKey("daily_per_t_yield") == null ? "0" : configService.selectConfigByKey("daily_per_t_yield");

        //电费
        String dailyPowerFee = configService.selectConfigByKey("daily_power_fee") == null ? "0" : configService.selectConfigByKey("daily_power_fee");

        //平台算力
        String platformTotalComputePower = configService.selectConfigByKey("platform_total_compute_power") == null ? "0" : configService.selectConfigByKey("platform_total_compute_power");

        HashMap<String, Object> vo = new HashMap<>();

        vo.put("dailyYieldPerT", dailyYieldPerT);
        vo.put("dailyPowerFee", dailyPowerFee);
        vo.put("platformTotalComputePower", platformTotalComputePower);
        return AjaxResult.success(vo);
    }




}
