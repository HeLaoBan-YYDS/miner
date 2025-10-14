package com.ruoyi;

import java.util.*;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.*;

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
        // 假设你的 JSON 数据存储在字符串 jsonStr 中
        String jsonStr = "{\n" +
                "    \"lucky\": null,\n" +
                "    \"blocklist\": [\n" +
                "        {\n" +
                "            \"height\": 901330,\n" +
                "            \"block_hash\": \"000000000000000000008e7e2e94a9c2c6d28ea42f49ad856a69dc862981eba6\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.14062731,\n" +
                "            \"timestamp\": 1749972219,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901342,\n" +
                "            \"block_hash\": \"00000000000000000000c9026900efa93f4e434011f8639fc792f2fb0e721d0c\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.12597536,\n" +
                "            \"timestamp\": 1749976826,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901358,\n" +
                "            \"block_hash\": \"00000000000000000001a1a059e547a4f7dedf3788bf47f230e7723a1336fc79\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.12568036,\n" +
                "            \"timestamp\": 1749988250,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901367,\n" +
                "            \"block_hash\": \"00000000000000000000508f2d7f9520e2e5004b59529afabcc34fe7ded5265a\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.14401031,\n" +
                "            \"timestamp\": 1749993423,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901418,\n" +
                "            \"block_hash\": \"000000000000000000018f7679cb5b2d356a00466d7341a7f7fa76f70507b794\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.12942761,\n" +
                "            \"timestamp\": 1750026668,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901420,\n" +
                "            \"block_hash\": \"000000000000000000002a4329be0d166dba740e72e6fe48c9a6bdd50e429583\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.1475959600000003,\n" +
                "            \"timestamp\": 1750029396,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901434,\n" +
                "            \"block_hash\": \"0000000000000000000214c2543f83d5c392457f7f274f8cc7c90509d6f40890\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.1305131900000003,\n" +
                "            \"timestamp\": 1750035544,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901455,\n" +
                "            \"block_hash\": \"000000000000000000008e5f414d35a0276210aec621ff9f89ddf6a47275f008\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.14030703,\n" +
                "            \"timestamp\": 1750047579,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901459,\n" +
                "            \"block_hash\": \"00000000000000000000c361f433dbc626ade3b573c437a78a67c570e5171c53\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.17712829,\n" +
                "            \"timestamp\": 1750052036,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901460,\n" +
                "            \"block_hash\": \"000000000000000000017eb966e8834d40ad1373f2816473c98c8d70ff807c31\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.13773745,\n" +
                "            \"timestamp\": 1750052089,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901499,\n" +
                "            \"block_hash\": \"00000000000000000000e3f2849280c1cc4669e72ae350485263675589a2ee47\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.12651807,\n" +
                "            \"timestamp\": 1750072274,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901504,\n" +
                "            \"block_hash\": \"00000000000000000000706b1005959e7eda721875ce145a78cf99964dfec948\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.13046059,\n" +
                "            \"timestamp\": 1750074336,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901518,\n" +
                "            \"block_hash\": \"00000000000000000000562adc9aebc2c67af58e1d3c814e7da9002e0e26472b\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.1285105,\n" +
                "            \"timestamp\": 1750081262,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901520,\n" +
                "            \"block_hash\": \"00000000000000000001fd31e9b6a0391088f8e42833d204d22926c84a9f7349\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.14320234,\n" +
                "            \"timestamp\": 1750082479,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901525,\n" +
                "            \"block_hash\": \"00000000000000000000351e156acbf2d0cd63d70226a1ddb941e4e45bf98abe\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.16813722,\n" +
                "            \"timestamp\": 1750085784,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901526,\n" +
                "            \"block_hash\": \"0000000000000000000146409352aaf4533c25a4a202ae65cffb768dff8faeba\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.1481507,\n" +
                "            \"timestamp\": 1750085974,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901542,\n" +
                "            \"block_hash\": \"00000000000000000000657a15db40e4630b43fd85ede081c9694ed400db04f8\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.20272447,\n" +
                "            \"timestamp\": 1750096971,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901549,\n" +
                "            \"block_hash\": \"0000000000000000000095aec1e64dc056528ec3693d84fb72a35832fa8eb166\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.1822219300000003,\n" +
                "            \"timestamp\": 1750102033,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901552,\n" +
                "            \"block_hash\": \"0000000000000000000239f2335533fec27ac29ee22a36a9319010bc6d7d0586\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.22743761,\n" +
                "            \"timestamp\": 1750106066,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901554,\n" +
                "            \"block_hash\": \"000000000000000000001876e4ed667677a7c5b5a530d5cd624aee303ff73e88\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.16951647,\n" +
                "            \"timestamp\": 1750106809,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901558,\n" +
                "            \"block_hash\": \"00000000000000000000cb94ae1b363da6010b55a48812fb6670a0649adaf94b\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.14618852,\n" +
                "            \"timestamp\": 1750107667,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901567,\n" +
                "            \"block_hash\": \"000000000000000000001e837d4e500e386acd5e04a7bb0a553cdea2337d6507\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.1531808100000003,\n" +
                "            \"timestamp\": 1750115467,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901581,\n" +
                "            \"block_hash\": \"00000000000000000000d9a0311eeaa3fcfe0f57854a840d33d81d52ca96d462\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.1973330200000003,\n" +
                "            \"timestamp\": 1750130113,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901644,\n" +
                "            \"block_hash\": \"00000000000000000001bae820adfd669e3d6dfabe285eb4caf4db9788b5760c\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.20406003,\n" +
                "            \"timestamp\": 1750175883,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901654,\n" +
                "            \"block_hash\": \"00000000000000000000c54acb8d1a59ed8029b82fb719953e0ff591814073be\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.1904588400000002,\n" +
                "            \"timestamp\": 1750183906,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901657,\n" +
                "            \"block_hash\": \"00000000000000000001301d0d0903a0d482d9dde1b5fad02126cd9ece2ee894\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.14598742,\n" +
                "            \"timestamp\": 1750184420,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901659,\n" +
                "            \"block_hash\": \"0000000000000000000209df89312ca7ade061b1afb280c29e42f2f2f8690fa1\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.15786393,\n" +
                "            \"timestamp\": 1750185100,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901713,\n" +
                "            \"block_hash\": \"000000000000000000003a9e16553439cd366461bb1958a24aaa5b05e2a58de7\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.14540946,\n" +
                "            \"timestamp\": 1750218858,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901720,\n" +
                "            \"block_hash\": \"000000000000000000021de5eb2eb57403fdc72fdea0bcfe2679a722641d9abf\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.13906697,\n" +
                "            \"timestamp\": 1750221241,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        },\n" +
                "        {\n" +
                "            \"height\": 901722,\n" +
                "            \"block_hash\": \"00000000000000000000749914c42a3e7247261e59a8cab1b9dd79c4d8c8e56d\",\n" +
                "            \"base_reward\": 3.125,\n" +
                "            \"total_reward\": 3.19017859,\n" +
                "            \"timestamp\": 1750222567,\n" +
                "            \"settle_timestamp\": 0\n" +
                "        }\n" +
                "    ],\n" +
                "    \"sum\": 0\n" +
                "}\n";

        // 使用 Jackson 解析 JSON
        ObjectMapper mapper = new ObjectMapper();
        BlockData data = JSONUtil.toBean(jsonStr, BlockData.class);

        double totalBaseReward = 0;
        double totalActualReward = 0;

        for (Block b : data.blocklist) {
            totalBaseReward += b.base_reward;
            totalActualReward += b.total_reward;
        }

        double luck = (totalActualReward / totalBaseReward) * 100;

        System.out.println("理论奖励总和: " + totalBaseReward);
        System.out.println("实际奖励总和: " + totalActualReward);
        System.out.println("Luck: " + luck + "%");
    }
}
