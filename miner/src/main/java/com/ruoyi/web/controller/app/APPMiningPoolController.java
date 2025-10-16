package com.ruoyi.web.controller.app;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.BtcPriceUtil;
import com.ruoyi.common.utils.HttpUtil;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.domain.Block;
import com.ruoyi.system.domain.BlockData;
import com.ruoyi.system.domain.vo.MiningMachineVO;
import com.ruoyi.system.service.IBizProjectService;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/app/pool")
@Api(tags = "矿池管理")
public class APPMiningPoolController {

    @Autowired
    RedisCache redisCache;

    @Autowired
    private IBizProjectService bizProjectService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private BtcPriceUtil btcPriceUtil;

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



    @GetMapping("/chart")
    @ApiOperation("图表数据")
    @Anonymous
    public String chart() {
        String redisKey = "chart";
        String req = redisCache.getCacheObject(redisKey);
        if (req != null) return req;
        String s = HttpUtil.doPost("https://www.f2pool.com/coins-chart?currency_code=btc&history_days=30d&interval=60m", "");
        redisCache.setCacheObject(redisKey,s,20, TimeUnit.MINUTES);
        return s;
    }


    @GetMapping("/list")
    @Anonymous
    @ApiOperation("矿机数据")
    private AjaxResult list() {
        String redisKey = "kjValue";
        AjaxResult req = redisCache.getCacheObject(redisKey);
        if (req != null) return req;
        BigDecimal unitFee = new BigDecimal(configService.selectConfigByKey("daily_power_fee"));
        BigDecimal unitYield = new BigDecimal(configService.selectConfigByKey("daily_per_t_yield"));
        BigDecimal h = new BigDecimal("24");
        BizProject bizProjectCondition = new BizProject();
        bizProjectCondition.setStatus(ProjectStatus.SUCCESS.getCode());
        List<BizProject> bizProjects = bizProjectService.selectBizProjectList(bizProjectCondition);
        ArrayList<MiningMachineVO> miningMachineVOS = new ArrayList<>();
        for (BizProject project : bizProjects) {
            //每日电费
            BigDecimal electricityFee = project.getPowerConsumption().multiply(unitFee).multiply(h).divide(project.getComputePower(), 2, RoundingMode.DOWN);
            BigDecimal outputBTC = project.getComputePower().multiply(unitYield);
            BigDecimal outputUSDT = btcPriceUtil.btcToUsdt(outputBTC);
            //电费占比
            BigDecimal electricityRatio = electricityFee.divide(outputUSDT, 2, RoundingMode.DOWN);
            // 日净利润
            BigDecimal dailyNetProfit = outputUSDT.subtract(electricityFee);
            MiningMachineVO miningMachineVO = new MiningMachineVO();
            miningMachineVO.setProjectName(project.getProjectName());
            miningMachineVO.setElectricityFee(electricityFee);
            miningMachineVO.setElectricityRatio(project.getPowerConsumption().divide(project.getComputePower(), 2, RoundingMode.DOWN));
            miningMachineVO.setElectricityRatio(electricityRatio);
            miningMachineVO.setDailyNetProfit(dailyNetProfit);
            miningMachineVO.setShutdownPrice(electricityFee.divide(outputUSDT,2, RoundingMode.DOWN));
            miningMachineVOS.add(miningMachineVO);
        }
        AjaxResult success = AjaxResult.success(miningMachineVOS);
        redisCache.setCacheObject(redisKey,success,5, TimeUnit.MINUTES);
        return success;
    }


}
