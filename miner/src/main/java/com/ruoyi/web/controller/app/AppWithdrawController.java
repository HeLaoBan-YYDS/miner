package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.IBizAddressService;
import com.ruoyi.system.service.IBizWithdrawService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/app/withdraw")
@Api(tags = "提现管理")
public class AppWithdrawController {



    @Autowired
    IBizWithdrawService withdrawService;



    @PostMapping("/withdrawal")
    @Anonymous
    public String transferCallback(@RequestBody  String  data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        String result = withdrawService.transferCallback(data);
        return  result;

    }



}
