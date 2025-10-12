package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.IBizAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/app/recharge")
@Api(tags = "充值管理")
@Slf4j
public class AppRechargeController {



    @Autowired
    IBizAddressService bizAddressService;


    /**
     * 充值获取地址
     * @return
     */
    @PreAuthorize("@ss.hasRole('user')")
    @GetMapping(value = "/address")
    @ApiOperation("获取充值地址")
    public AjaxResult getAddress(String coinType)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (null == loginUser || loginUser.getUserId() == null ){
            return AjaxResult.error(MessageUtils.message("user.notfound"));
        }

        String address =bizAddressService.getAddress(loginUser,coinType);
        if(null == address){
          return AjaxResult.error(MessageUtils.message("get.address.fail"));
        }
        return AjaxResult.success(address);
    }


    /**
     * 充值回调
     * @return
     */
    @PostMapping("/callback")
    @Anonymous
    @ApiOperation("充值回调")
    public String callBack(@RequestBody  String  data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        String result = bizAddressService.callBack(data);
        return  result;
    }


    /**
     * 风控回调
     * @return
     */
    @PostMapping("/riskCallback")
    @Anonymous
    @ApiOperation("提现风控回调")
    public String riskCallback(@RequestBody  String  data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        log.info("风控回调:{}",data);
        return bizAddressService.riskCallback(data);

    }
}
