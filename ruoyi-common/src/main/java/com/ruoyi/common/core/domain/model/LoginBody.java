package com.ruoyi.common.core.domain.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户登录对象
 *
 * @author ruoyi
 */
public class LoginBody
{
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名(传递邮箱)", required = true)
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码", required = true)
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码",required = true)
    private String code;

    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "唯一标识(传递邮箱)", required = true)
    private String uuid;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }
}
