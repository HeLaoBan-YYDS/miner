package com.ruoyi.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 币种类型枚举
 */
@Getter
public enum CoinType {
    AIU("aiu", "AIU", "系统主币种", 8),
    AIUX("aiux", "AIUX", "系统副币种", 8),
    USDT("usdt", "USDT", "泰达币", 6);

    /**
     * 数据库存储值
     */
    private final String code;

    /**
     * 币种符号
     */
    private final String symbol;

    /**
     * 币种描述
     */
    private final String description;

    /**
     * 默认小数位数
     */
    private final int defaultScale;

    CoinType(String code, String symbol, String description, int defaultScale) {
        this.code = code;
        this.symbol = symbol;
        this.description = description;
        this.defaultScale = defaultScale;
    }

    /**
     * 根据code获取枚举
     */
    public static Optional<CoinType> getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(e -> e.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    /**
     * 检查是否为有效币种
     */
    public static boolean isValid(String code) {
        return getByCode(code).isPresent();
    }

    /**
     * 获取币种符号
     */
    public static String getSymbol(String code) {
        return getByCode(code).map(CoinType::getSymbol).orElse("");
    }

    /**
     * 获取默认小数位数
     */
    public static int getDefaultScale(String code) {
        return getByCode(code).map(CoinType::getDefaultScale).orElse(8);
    }

    @Override
    public String toString() {
        return this.code;
    }
}
