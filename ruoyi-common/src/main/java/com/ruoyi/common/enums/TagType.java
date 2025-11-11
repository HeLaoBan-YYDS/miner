package com.ruoyi.common.enums;

public enum TagType {
    EXCLUSIVE_FOR_NEW_USERS("0", "新用户专属"),
    HOT_SELLING_TOP1("1", "热销TOP1"),
    HOT_SELLING_TOP2("2", "热销TOP2"),
    HOT_SELLING_TOP3("3", "热销TOP3"),
    HOT_SELLING_TOP4("4", "热销TOP4"),
    HOT_SELLING("11", "热销");

    private final String code;
    private final String info;

    TagType(String code, String info)
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
