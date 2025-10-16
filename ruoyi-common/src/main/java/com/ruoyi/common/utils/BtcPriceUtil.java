package com.ruoyi.common.utils;

import com.ruoyi.common.core.redis.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@Component
public class BtcPriceUtil {

    private static final String BINANCE_API = "https://data-api.binance.vision/api/v3/ticker/price?symbol=BTCUSDT";
    private static final String REDIS_KEY = "BTC_USDT_PRICE";
    private static final Integer CACHE_SECONDS = 5;
    private static final Logger log = LoggerFactory.getLogger(BtcPriceUtil.class);


    @Autowired
    private RedisCache redisCache;

    /**
     * 获取当前BTC对USDT价格，缓存60秒
     */
    public BigDecimal getBtcUsdtPrice() {
        String priceStr = redisCache.getCacheObject(REDIS_KEY);
        if (priceStr != null) {
            return new BigDecimal(priceStr);
        }
        try {
            String result = HttpUtil.doGet(BINANCE_API);
            String price = result.split("\"price\":\"")[1].split("\"")[0];
            redisCache.setCacheObject(REDIS_KEY, price, CACHE_SECONDS, TimeUnit.MINUTES);
            return new BigDecimal(price);
        } catch (Exception e) {
            log.error("获取BTC价格失败", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * USDT转BTC
     */
    public BigDecimal usdtToBtc(BigDecimal usdtAmount) {
        BigDecimal price = getBtcUsdtPrice();
        if (usdtAmount == null || price.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return usdtAmount.divide(price, 8, RoundingMode.DOWN);
    }

    /**
     * BTC转USDT
     */
    public BigDecimal btcToUsdt(BigDecimal btcAmount) {
        BigDecimal price = getBtcUsdtPrice();
        if (btcAmount == null) {
            return BigDecimal.ZERO;
        }
        return btcAmount.multiply(price).setScale(8, RoundingMode.DOWN);
    }
}
