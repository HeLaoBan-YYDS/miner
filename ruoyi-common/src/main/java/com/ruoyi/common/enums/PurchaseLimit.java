package com.ruoyi.common.enums;

public enum PurchaseLimit {
    LIMIT_ONE_PURCHASE("1", "限购一次"), CAN_BE_REPURCHASED("2", "可复购");

    private final String code;
    private final String info;

    PurchaseLimit(String code, String info)
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
