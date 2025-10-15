package com.ruoyi.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * 币种类型枚举
 */
@Getter
public enum CoinType {
    USDT("usdt", "usdt_trc20", "泰达币", 8,"usdt_fee"),
    BTC("btc", "btc", "比特币", 8,"btc_fee");

    /**
     * 数据库存储值
     */
    private final String code;

    /**
     * 币种符号
     */
    private final String coin;

    /**
     * 币种描述
     */
    private final String description;

    /**
     * 默认小数位数
     */
    private final int defaultScale;

    private final String fee;

    CoinType(String code, String coin, String description, int defaultScale, String fee) {
        this.code = code;
        this.coin = coin;
        this.description = description;
        this.defaultScale = defaultScale;
        this.fee = fee;
    }

    /**
     * 根据code获取枚举
     */
    public static Optional<CoinType> getByCode(String code) {
        if ("usdt_trc20".equals(code)) {
            code = "usdt";
        }

        if (StringUtils.isBlank(code)) {
            return Optional.empty();
        }
        String finalCode = code;
        return Arrays.stream(values())
                .filter(e -> e.getCode().equalsIgnoreCase(finalCode))
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
    public static String getCoin(String code) {
        return getByCode(code).map(CoinType::getCoin).orElse("");
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
