package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.dto.PlaceDTO;
import com.ruoyi.system.service.IBizOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/app/order")
@Api(tags = "app订单管理")
public class AppOrderController extends BaseController {

    @Autowired
    private IBizOrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("@ss.hasRole('user')")
    @Log(title = "下单", businessType = BusinessType.UPDATE)
    @ApiOperation("下单")
    @RepeatSubmit
    public AjaxResult place(@RequestBody @Valid PlaceDTO placeDTO) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Assert.notNull(loginUser, MessageUtils.message("user.notfound"));
        placeDTO.setUserId(loginUser.getUserId());
        orderService.place(placeDTO);
        return success();
    }


}
