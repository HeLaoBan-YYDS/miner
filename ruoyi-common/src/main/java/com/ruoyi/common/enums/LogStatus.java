package com.ruoyi.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 日志状态枚举
 * 状态值对应：
 * 0=待审核
 * 1=成功
 * 2=失败
 * 3=审核通过
 * 4=审核拒绝
 * 5=处理中
 */
@Getter
public enum LogStatus {
    PENDING("0", "待审核", "warning", "等待审核状态"),
    SUCCESS("1", "成功", "success", "操作成功完成"),
    FAILED("2", "审核不通过", "danger", "审核不通过");

    /**
     * 数据库存储值
     */
    private final String code;

    /**
     * 状态名称
     */
    private final String name;

    /**
     * 前端标签样式类型
     */
    private final String tagType;

    /**
     * 状态描述
     */
    private final String description;

    LogStatus(String code, String name, String tagType, String description) {
        this.code = code;
        this.name = name;
        this.tagType = tagType;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     */
    public static Optional<LogStatus> getByCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst();
    }

    /**
     * 检查是否为有效状态
     */
    public static boolean isValid(String code) {
        return getByCode(code).isPresent();
    }

    /**
     * 获取状态名称
     */
    public static String getName(String code) {
        return getByCode(code).map(LogStatus::getName).orElse("");
    }

    /**
     * 获取前端标签样式
     */
    public static String getTagType(String code) {
        return getByCode(code).map(LogStatus::getTagType).orElse("");
    }

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }
}
