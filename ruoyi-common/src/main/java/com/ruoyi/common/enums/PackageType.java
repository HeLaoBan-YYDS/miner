package com.ruoyi.common.enums;

public enum PackageType {
    NOVICE_PACKAGE("NOVICE.PACKAGE", "新手套餐"), MEMBERSHIP_PACKAGE("MEMBERSHIP.PACKAGE", "会员套餐");

    private final String code;
    private final String info;

    PackageType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
