package com.ruoyi.common.enums;

public enum ComputePowerType {
    FIXED_COMPUTING_POWER("FIXED.COMPUTING.POWER", "固定算力"), MINIMUM_STARTING_PRICE("MINIMUM.STARTING.PRICE", "最低起售");

    private final String code;
    private final String info;

    ComputePowerType(String code, String info)
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
