package com.ruoyi.web.controller.app;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SysBanner;
import com.ruoyi.system.service.ISysBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "APP Banner图")
@RequestMapping("/app/banner")
public class AppBannerController extends BaseController {

    @Autowired
    private ISysBannerService sysBannerService;

    @PreAuthorize("@ss.hasPermi('user')")
    @GetMapping("/list")
    @ApiOperation(value = "Banner列表")
    public TableDataInfo<List<SysBanner>> list(String language)
    {
        SysBanner sysBanner = new SysBanner();
        sysBanner.setLanguage(language);
        List<SysBanner> list = sysBannerService.selectSysBannerList(sysBanner);
        return getDataTable(list);
    }
}
