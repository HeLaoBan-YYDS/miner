package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.*;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@RestController
@Api(tags = "APP注册管理")
public class AppRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    @Value("${external.hotWallet.ownerPriKey}")
    public String ownerPriKey;

    @PostMapping("/app/register")
    @Anonymous
    @Log(title = "注册", businessType = BusinessType.OTHER)
    @ApiOperation("用户注册")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("app.account.registerUser")))) {
            return error(MessageUtils.message("system.register.disabled"));
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }


    @GetMapping("/app/sendSmsCode")
    @Anonymous
    @ApiOperation("发送邮箱验证码")
    public AjaxResult sendSmsCode(@ApiParam(value = "RSA算法基于邮箱生成的签名",required = true) String sign) {

        String email;
        try {
            email = SignUtil.decrypt(sign, ownerPriKey);
        } catch (Exception e) {
            return error("解密失败-非法攻击");
        }

        boolean validEmail = EmailValidator.isValidEmail(email);
        if (!validEmail) {
            return error(MessageUtils.message("user.email.not.valid"));
        }

        //生成验证码
        String validCode = InvitationCodeGenerator.generateInvitationCode();

        if (Objects.equals(MailUtil.sendMail(email, validCode, "24Miner"), "0")) {
            String redisKey  = CacheConstants.CAPTCHA_CODE_KEY + email;
            redisCache.setCacheObject(redisKey, validCode, 5, TimeUnit.MINUTES);
            return AjaxResult.success();
        } else {
            return error(MessageUtils.message("mail.send.fail"));
        }
    }

}
