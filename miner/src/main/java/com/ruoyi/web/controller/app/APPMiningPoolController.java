package com.ruoyi.web.controller.app;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.HttpUtil;
import com.ruoyi.system.domain.Block;
import com.ruoyi.system.domain.BlockData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/app/pool")
@Api(tags = "矿池管理")
public class APPMiningPoolController {

    @Autowired
    RedisCache redisCache;

    /**
     * 查询项目基本信息列表
     */
    @GetMapping("/luck")
    @ApiOperation("幸运值")
    @Anonymous
    public AjaxResult luck() {
        String redisKey = "luckValue";
        AjaxResult req = redisCache.getCacheObject(redisKey);
        if (req != null) {
            return req;
        }
        double luckValue3Day = getLuckValue(-3);
        double luckValue7Day = getLuckValue(-7);
        HashMap<Object, Object> vo = new HashMap<>();
        vo.put("threeDay", luckValue3Day);
        vo.put("sevenDay", luckValue7Day);
        AjaxResult success = AjaxResult.success(vo);
        redisCache.setCacheObject(redisKey,success,5, TimeUnit.MINUTES);
        return success;
    }

    private double getLuckValue(int beforeDay) {
        long end_time = new Date().getTime();
        long start_time = DateUtil.offsetDay(new Date(), beforeDay).getTime();
        HashMap<Object, Object> parameter = new HashMap<>();
        parameter.put("start_time", start_time / 1000);
        parameter.put("end_time", end_time / 1000);
        parameter.put("currency", "bitcoin");
        String re3 = HttpUtil.doPost("https://api.f2pool.com/v2/blocks/date_range", JSONUtil.toJsonStr(parameter));
        return calLuckValue(re3);
    }

    private double calLuckValue(String re3) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double totalBaseReward = 0;
        double totalActualReward = 0;

        BlockData block3Day = JSONUtil.toBean(re3, BlockData.class);

        for (Block b : block3Day.getBlocklist()) {
            totalBaseReward += b.getBase_reward();
            totalActualReward += b.getTotal_reward();
        }

        return (totalActualReward / totalBaseReward) * 100;
    }
}
