package com.ruoyi.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

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
public enum ProjectStatus {
    SUCCESS("0", "上架" ),
    FAILED("1", "下架");

    /**
     * 数据库存储值
     */
    private final String code;

    /**
     * 状态名称
     */
    private final String name;



    ProjectStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static Optional<ProjectStatus> getByCode(String code) {
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
        return getByCode(code).map(ProjectStatus::getName).orElse("");
    }


    @Override
    public String toString() {
        return String.valueOf(this.code);
    }
}
