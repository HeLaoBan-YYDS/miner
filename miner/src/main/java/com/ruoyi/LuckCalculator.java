package com.ruoyi;

import java.util.*;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.*;
import com.ruoyi.common.utils.HttpUtil;

public class LuckCalculator {

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
        System.out.println(HttpUtil.doPost("https://www.f2pool.com/coins-chart?currency_code=btc&history_days=30d&interval=60m", ""));
    }
}
