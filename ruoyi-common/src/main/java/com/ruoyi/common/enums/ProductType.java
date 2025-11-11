package com.ruoyi.common.enums;

public enum ProductType {
    CLOUD_COMPUTING_POWER("CLOUD.COMPUTING.POWER", "云算力"), MINING_MACHINE("MINING.MACHINE", "矿机");

    private final String code;
    private final String info;

    ProductType(String code, String info)
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
