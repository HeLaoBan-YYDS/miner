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
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error(MessageUtils.message("system.register.disabled"));
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }


    @GetMapping("/app/sendSmsCode")
    @Log(title = "发送验证码", businessType = BusinessType.OTHER)
    @Anonymous
    public AjaxResult sendSmsCode(String sign) {

        String email;
        try {
            email = SignUtil.decrypt(sign, ownerPriKey);
        } catch (Exception e) {
            return error("解密失败");
        }

        boolean validEmail = EmailValidator.isValidEmail(email);
        if (!validEmail) {
            return error("邮箱格式不正确");
        }

        //生成验证码
        String validCode = InvitationCodeGenerator.generateInvitationCode();

        if (Objects.equals(MailUtil.sendMail(email, validCode, "24Miner"), "0")) {
            String redisKey  = CacheConstants.CAPTCHA_CODE_KEY + email;
            redisCache.setCacheObject(redisKey, validCode, 5, TimeUnit.MINUTES);
            return AjaxResult.success();
        } else {
            return error("邮件发送失败");
        }
    }

}
