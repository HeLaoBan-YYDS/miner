package com.ruoyi.common.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
public enum OrderStatusEnum {

    PAID("paid", "已支付"),
    EARNING("earning", "收益中"),
    POWER_ARREARS("power_arrears", "电费欠费"),
    ENDED("ended", "收益结束");

    private final String code;
    private final String info;

    OrderStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 根据code获取枚举
     */
    public static OrderStatusEnum getByCode(String code) {
        for (OrderStatusEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断是否有效状态
     */
    public static boolean isValid(String code) {
        return getByCode(code) != null;
    }
}
