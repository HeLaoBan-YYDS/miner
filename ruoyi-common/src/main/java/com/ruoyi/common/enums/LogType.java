package com.ruoyi.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 日志类型枚举
 */
@Getter
public enum LogType {
    RECHARGE("recharge", "充值", "primary", "用户充值操作"),
    WITHDRAW("withdraw", "提现", "warning", "用户提现操作"),
    PURCHASE("purchase", "购买产品", "success", "用户购买产品"),
    REFUND("refund", "退款", "info", "用户退款操作"),
    POWER_FEE("powerFee", "扣电费", "success", "扣电费"),
    INCOME("income", "收益", "success", "收益");


    /**
     * 数据库存储值
     */
    private final String code;

    /**
     * 类型名称
     */
    private final String name;

    /**
     * 前端显示样式
     */
    private final String tagType;

    /**
     * 类型描述
     */
    private final String description;

    LogType(String code, String name, String tagType, String description) {
        this.code = code;
        this.name = name;
        this.tagType = tagType;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static Optional<LogType> getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(e -> e.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    /**
     * 检查是否为有效日志类型
     */
    public static boolean isValid(String code) {
        return getByCode(code).isPresent();
    }

    /**
     * 获取类型名称
     */
    public static String getName(String code) {
        return getByCode(code).map(LogType::getName).orElse("");
    }

    /**
     * 获取前端标签样式
     */
    public static String getTagType(String code) {
        return getByCode(code).map(LogType::getTagType).orElse("");
    }

    /**
     * 转换为字典格式（用于前端下拉框）
     */
    public static Map<String, String> toDictData() {
        return Arrays.stream(values())
                .collect(Collectors.toMap(
                        LogType::getCode,
                        LogType::getName
                ));
    }

    @Override
    public String toString() {
        return this.code;
    }
}
