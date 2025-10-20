package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.BtcPriceUtil;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.domain.vo.AprInfoVo;
import com.ruoyi.system.domain.vo.ProjectVo;
import com.ruoyi.system.service.IBizProjectService;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 项目基本信息Controller
 *
 * @author Remi
 * @date 2025-09-16
 */
@RestController
@RequestMapping("/app/project")
@Api(tags = "APP端项目基本信息")
public class AppBizProjectController extends BaseController {
    @Autowired
    private IBizProjectService bizProjectService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private BtcPriceUtil btcPriceUtil;

    /**
     * 查询项目基本信息列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "项目列表", response = ProjectVo.class)
    @Anonymous
    public TableDataInfo<List<ProjectVo>> list() {
        BizProject bizProject = new BizProject();
        //每日单T产出(BTC)
        String dailyYieldPerTStr = configService.selectConfigByKey("daily_per_t_yield") == null ? "0" : configService.selectConfigByKey("daily_per_t_yield");
        BigDecimal dailyYieldPerT = new BigDecimal(dailyYieldPerTStr);

        //btc币价
        BigDecimal btcUsdtPrice = btcPriceUtil.getBtcUsdtPrice();

        //电费
        String unitPowerFeeStr = configService.selectConfigByKey("daily_power_fee") == null ? "0" : configService.selectConfigByKey("daily_power_fee");
        BigDecimal unitPowerFee = new BigDecimal(unitPowerFeeStr);

        //每日小时
        BigDecimal h = new BigDecimal("24");

        //年
        BigDecimal year = new BigDecimal("365");

        //百分比
        BigDecimal percent = new BigDecimal("100");

        startPage();
        String currentLang = MessageUtils.getCurrentLang();
        bizProject.getParams().put("langCode", currentLang);
        bizProject.setStatus(ProjectStatus.SUCCESS.getCode());
        List<ProjectVo> list = bizProjectService.selectProjectVoList(bizProject);
        for (ProjectVo projectVo : list) {
            //每日电费 = 功率 * 单价 * 小时 / 总算力
            BigDecimal dailyPowerCost = projectVo.getPowerConsumption()
                    .multiply(unitPowerFee)
                    .multiply(h)
                    .divide(projectVo.getComputePower(), 2, RoundingMode.DOWN);

            //年化率 = 每日产出(BTC) * BTC价格 - 每日电费 * 365 / 单价T算力价格 * 100
            BigDecimal apr = dailyYieldPerT
                    .multiply(btcUsdtPrice)
                    .subtract(dailyPowerCost)
                    .multiply(year)
                    .divide(projectVo.getUnitPrice(), 4, RoundingMode.DOWN)
                    .multiply(percent)
                    .setScale(2, RoundingMode.DOWN);

            projectVo.setApr(apr);
            BigDecimal aprNoPercent = apr.divide(percent,4,RoundingMode.HALF_UP);
            projectVo.setReturnAmount(year.divide(aprNoPercent,2,RoundingMode.HALF_UP));
        }
        return getDataTable(list);
    }

    /**
     * 查询项目基本信息列表
     */
    @GetMapping("/apr")
    @ApiOperation("APR")
    @Anonymous
    public R<AprInfoVo> apr() {
        //每日单T产出
        String dailyYieldPerT = configService.selectConfigByKey("daily_per_t_yield") == null ? "0" : configService.selectConfigByKey("daily_per_t_yield");

        //电费
        String dailyPowerFee = configService.selectConfigByKey("daily_power_fee") == null ? "0" : configService.selectConfigByKey("daily_power_fee");

        //平台算力
        String platformTotalComputePower = configService.selectConfigByKey("platform_total_compute_power") == null ? "0" : configService.selectConfigByKey("platform_total_compute_power");

        AprInfoVo aprInfoVo = new AprInfoVo();
        aprInfoVo.setDailyPowerFee(dailyPowerFee);
        aprInfoVo.setDailyYieldPerT(dailyYieldPerT);
        aprInfoVo.setPlatformTotalComputePower(platformTotalComputePower);
        return R.ok(aprInfoVo);
    }


}
