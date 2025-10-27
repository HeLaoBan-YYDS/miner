package com.ruoyi.web.controller.app;


import cn.hutool.core.util.StrUtil;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("/app/google")
@Api(tags = "APP端谷歌回调")
public class AppGoogleController {

    @Autowired
    private com.ruoyi.system.service.ISysUserService userService;

    @Autowired
    private com.ruoyi.framework.web.service.TokenService tokenService;

    @Autowired
    private ISysRoleService roleService;


    @Autowired
    private ISysMenuService menuService;



    @Value("${oauth.id}")
    private String ID;

    @Value("${oauth.secret}")
    private String SECRET;

    @Value("${oauth.uri}")
    private String URI;


    @GetMapping("/oauth/callback")
    @Anonymous
    @ApiOperation("谷歌登录回调")
    public AjaxResult callback(@RequestParam("code") String code) throws IOException {
        // 1. 交换 access token
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
                        ID,
                        SECRET,
                        code,
                        URI
                ).execute();

        // 2. 解析 ID token
        GoogleIdToken idToken = tokenResponse.parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        // 3. 本地数据库检查
        SysUser user = userService.selectUserByUserName(email);
        if (user == null) {
            user = new SysUser();
            user.setUserName(email);
            user.setNickName(name);
            user.setEmail(email);
            user.setAvatar(picture);
            user.setPassword(SecurityUtils.encryptPassword(UUID.randomUUID().toString()));
            userService.insertUser(user);
            user.setRoleIds(new Long[]{2L});
            userService.insertUserRole(user);
        }


        SysRole sysRole = roleService.selectRoleById(2L);
        user.setRoles(Collections.singletonList(sysRole));
        Set<String> permissions = menuService.selectMenuPermsByRoleId(2L);
        LoginUser loginUser = new LoginUser(user.getUserId(),null,user, permissions);


        // 4. 生成若依 JWT token
        String token = tokenService.createToken(loginUser);


        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);

        // 5. 返回结果（前端会保存 token）
        return ajax;
    }

    @GetMapping("/oauth/callback")
    @Anonymous
    @ApiOperation("Google 登录回调（Web + App 通用）")
    public AjaxResult callback(
            @RequestParam(value = "code Web端传递", required = false) String code,
            @RequestParam(value = "idToken APP端传递", required = false) String idTokenFromApp
    ) throws IOException, GeneralSecurityException {

        String email;
        String name;
        String picture;

        GoogleIdToken idToken = null;

        // =============== ① Web 登录流程：通过 code 换 token ===============
        if (StrUtil.isNotBlank(code)) {
            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            "https://oauth2.googleapis.com/token",
                            ID,
                            SECRET,
                            code,
                            URI
                    ).execute();

            idToken = tokenResponse.parseIdToken();
        }

        // =============== ② App 登录流程：直接验证 idToken ===============
        else if (StrUtil.isNotBlank(idTokenFromApp)) {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Arrays.asList(
                            "885390444574-4eogk26lc5namd1v7mmc8kfl92u0f4do.apps.googleusercontent.com"
                    ))
                    .build();

            idToken = verifier.verify(idTokenFromApp);
            if (idToken == null) {
                return AjaxResult.error("Invalid Google ID Token (App)");
            }
        }

        // =============== ③ 通用部分：解析用户信息 ===============
        if (idToken == null) {
            return AjaxResult.error("Missing or invalid Google token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        email = payload.getEmail();
        name = (String) payload.get("name");
        picture = (String) payload.get("picture");

        // =============== ④ 本地数据库同步用户 ===============
        SysUser user = userService.selectUserByUserName(email);
        if (user == null) {
            user = new SysUser();
            user.setUserName(email);
            user.setNickName(name);
            user.setEmail(email);
            user.setAvatar(picture);
            user.setPassword(SecurityUtils.encryptPassword(UUID.randomUUID().toString()));
            userService.insertUser(user);
            user.setRoleIds(new Long[]{2L});
            userService.insertUserRole(user);
        }

        // 加载角色和权限
        SysRole sysRole = roleService.selectRoleById(2L);
        user.setRoles(Collections.singletonList(sysRole));
        Set<String> permissions = menuService.selectMenuPermsByRoleId(2L);
        LoginUser loginUser = new LoginUser(user.getUserId(), null, user, permissions);

        // =============== ⑤ 生成若依 JWT Token ===============
        String token = tokenService.createToken(loginUser);

        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }






    /**
     * 前端获取 Google 登录 URL
     */
    @GetMapping("/login-url")
    @Anonymous
    @ApiOperation("获取谷歌登录地址")
    public AjaxResult getLoginUrl() {
        String url = new GoogleAuthorizationCodeRequestUrl(
                ID,
                URI,
                Arrays.asList("email", "profile"))
                .setAccessType("offline")
                .build();
        return AjaxResult.success(url);
    }


}
