package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.dto.WithdrawDTO;
import com.ruoyi.system.service.IBizWithdrawService;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/app/withdraw")
@Api(tags = "APP端提现管理")
public class AppWithdrawController {

    @Autowired
    private IBizWithdrawService withdrawService;

    @Autowired
    private ISysConfigService configService;

    @PostMapping("/callback")
    @Anonymous
    public String transferCallback(@RequestBody String data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        return withdrawService.withdrawCallback(data);
    }

    @GetMapping("/fee")
    @Anonymous
    public AjaxResult withdrawFee(String  coin) {
        String value = configService.selectConfigByKey(coin);
        return AjaxResult.success(value);
    }


    @PostMapping
    @PreAuthorize("@ss.hasRole('user')")
    @Log(title = "提现", businessType = BusinessType.UPDATE)
    @ApiOperation("提现")
    @RepeatSubmit
    public AjaxResult withdraw(@RequestBody @Valid WithdrawDTO withdrawDTO) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Assert.notNull(loginUser, MessageUtils.message("user.notfound"));
        withdrawDTO.setUserId(loginUser.getUserId());
        withdrawService.withdraw(withdrawDTO);
        return AjaxResult.success();
    }


}
