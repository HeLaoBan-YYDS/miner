package com.ruoyi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.*;
import com.ruoyi.common.utils.HttpUtil;

public class LuckCalculator {

    private static final String BINANCE_API = "https://data-api.binance.vision/api/v3/ticker/price?symbol=BTCUSDT";


    public class Block {
        public int height;
        public String block_hash;
        public double base_reward;
        public double total_reward;
        public long timestamp;
        public long settle_timestamp;
    }

    static class BlockData {
        public List<Block> blocklist;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(usdtToBtc(new BigDecimal("1693.4400000000")));
    }


    /**
     * USDT转BTC
     */
    public static BigDecimal usdtToBtc(BigDecimal usdtAmount) {
        BigDecimal price = getBtcUsdtPrice();
        if (usdtAmount == null || price.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return usdtAmount.divide(price, 8, RoundingMode.DOWN);
    }


    public static BigDecimal getBtcUsdtPrice() {
        try {
            String result = HttpUtil.doGet(BINANCE_API);
            String price = result.split("\"price\":\"")[1].split("\"")[0];
            return new BigDecimal(price);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
