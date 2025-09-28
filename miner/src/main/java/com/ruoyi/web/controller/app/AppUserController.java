package com.ruoyi.web.controller.app;


import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OrderStatusEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.domain.BizOrder;
import com.ruoyi.system.domain.vo.UserInfoVo;
import com.ruoyi.system.service.IBizOrderService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/app/user")
@Api(tags = "app用户管理")
public class AppUserController extends BaseController {


    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IBizOrderService orderService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 重置密码
     */
    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    @Anonymous
    @ApiOperation("修改密码")
    public AjaxResult updatePwd(@RequestBody LoginBody user) {
        registerService.validateCaptcha(null, user.getCode(), user.getUsername());

        SysUser loginUser = userService.selectUserByUserName(user.getUsername());
        if (loginUser == null) {
            return error(MessageUtils.message("user.notfound"));
        }

        Long userId = loginUser.getUserId();
        String newPassword = SecurityUtils.encryptPassword(user.getPassword());
        if (userService.resetUserPwd(userId, newPassword) > 0) {
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }


    @GetMapping("/info")
    @PreAuthorize("@ss.hasRole('user')")
    @ApiOperation("用户信息")
    public AjaxResult userinfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return error(MessageUtils.message("user.notfound"));
        }

        //查询我的订单
        BizOrder bizOrder = new BizOrder();
        bizOrder.setUserId(loginUser.getUserId());
        bizOrder.setDelFlag("0");
        List<BizOrder> bizOrders = orderService.selectBizOrderList(bizOrder);

        //查询我的信息
        SysUser sysUser = userService.selectUserById(loginUser.getUserId());
        if (sysUser == null) {
            return error(MessageUtils.message("user.notfound"));
        }

        //我的总算力
        BigDecimal myTotalComputePower = bizOrders.stream()
                .filter(order -> !OrderStatusEnum.ENDED.getCode().equals(order.getStatus()))
                .map(BizOrder::getComputePower)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //每日单T产出
        String dailyYieldPerT = configService.selectConfigByKey("daily_per_t_yield");

        //电费
        String dailyPowerFee = configService.selectConfigByKey("daily_power_fee");

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setDailyYieldPerT(dailyYieldPerT);
        userInfoVo.setMyTotalComputePower(myTotalComputePower);
        userInfoVo.setUsdtAccount(sysUser.getAccount());
        userInfoVo.setBtcAccount(sysUser.getBtcAccount());
        userInfoVo.setDailyPowerFee(dailyPowerFee);
        return success(userInfoVo);
    }
}
