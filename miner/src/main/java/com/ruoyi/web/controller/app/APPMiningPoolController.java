package com.ruoyi.web.controller.app;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ProjectStatus;
import com.ruoyi.common.utils.BtcPriceUtil;
import com.ruoyi.common.utils.HttpUtil;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.system.domain.BizProject;
import com.ruoyi.system.domain.Block;
import com.ruoyi.system.domain.BlockData;
import com.ruoyi.system.domain.LuckValueVO;
import com.ruoyi.system.domain.dto.MiningProfitDTO;
import com.ruoyi.system.domain.vo.DefaultParamsVO;
import com.ruoyi.system.domain.vo.MiningMachineVO;
import com.ruoyi.system.domain.vo.PoolInfoVO;
import com.ruoyi.system.service.IBizProjectService;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Api(tags = "APP端矿池管理")
public class APPMiningPoolController {

    private static final Logger log = LoggerFactory.getLogger(APPMiningPoolController.class);
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
    public R<LuckValueVO> luck() {
        String redisKey = "luckValue";
        R<LuckValueVO> req = redisCache.getCacheObject(redisKey);
        if (req != null) {
            return req;
        }
        double luckValue3Day = getLuckValue(-3);
        double luckValue7Day = getLuckValue(-7);
        LuckValueVO vo = new LuckValueVO();
        vo.setThreeDay(luckValue3Day);
        vo.setSevenDay(luckValue7Day);
        R<LuckValueVO> success = R.ok(vo);
        redisCache.setCacheObject(redisKey, success, 10, TimeUnit.MINUTES);
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
        redisCache.setCacheObject(redisKey, s, 20, TimeUnit.MINUTES);
        return s;
    }


    @GetMapping("/list")
    @Anonymous
    @ApiOperation("矿机数据")
    private R<ArrayList<MiningMachineVO>> list(MiningProfitDTO dto) {
        String redisKey = "kjValue:"+dto.getCoinPrice()+":"+dto.getElectricityPrice()+":"+dto.getSearchKeyword();
        R<ArrayList<MiningMachineVO>> req = redisCache.getCacheObject(redisKey);
        if (req != null) return req;
        //电费
        BigDecimal unitFee = BigDecimal.ZERO;
        if (dto.getElectricityPrice() != null) {
            unitFee = dto.getElectricityPrice();
        }else {
            unitFee = new BigDecimal(configService.selectConfigByKey("daily_power_fee"));
        }
        BigDecimal unitYield = new BigDecimal(configService.selectConfigByKey("daily_per_t_yield"));

        BigDecimal h = new BigDecimal("24");
        BizProject bizProjectCondition = new BizProject();
        bizProjectCondition.setStatus(ProjectStatus.SUCCESS.getCode());
        bizProjectCondition.setProjectName(dto.getSearchKeyword());
        List<BizProject> bizProjects = bizProjectService.selectBizProjectList(bizProjectCondition);
        ArrayList<MiningMachineVO> miningMachineVOS = new ArrayList<>();
        for (BizProject project : bizProjects) {
            //每日电费
            BigDecimal electricityFee = project.getPowerConsumption().multiply(unitFee).multiply(h).divide(project.getComputePower(), 2, RoundingMode.DOWN);
            BigDecimal outputBTC = project.getComputePower().multiply(unitYield);


            BigDecimal outputUSDT = BigDecimal.ZERO;
            if (dto.getCoinPrice() != null){
                outputUSDT = outputBTC.multiply(dto.getCoinPrice());
            }else {
                outputUSDT = btcPriceUtil.btcToUsdt(outputBTC);
            }

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
            miningMachineVO.setShutdownPrice(electricityFee.divide(outputBTC, 2, RoundingMode.DOWN));
            miningMachineVOS.add(miningMachineVO);
        }
        R<ArrayList<MiningMachineVO>> success = R.ok(miningMachineVOS);

        redisCache.setCacheObject(redisKey, success, 5, TimeUnit.MINUTES);
        return success;
    }


    @GetMapping("/info")
    @ApiOperation("矿池基础信息")
    @Anonymous
    public R<PoolInfoVO> poolInfo() {
        String redisKey = "poolInfo";
//        R<PoolInfoVO> cachedResult = redisCache.getCacheObject(redisKey);
//        if (cachedResult != null) {
//            return cachedResult;
//        }

        try {
            // 获取区块链数据
            String response = HttpUtil.doGet("https://api.blockchair.com/bitcoin/stats");
            JSONObject jsonData = JSONUtil.parseObj(response);
            JSONObject data = jsonData.getJSONObject("data");

            // 提取所需信息并封装到VO对象
            PoolInfoVO poolInfoVO = new PoolInfoVO();

            // 总币数单位转换：从聪(satoshi)转换为BTC
            Long circulationSatoshi = data.getLong("circulation");
            BigDecimal circulationBTC = new BigDecimal(circulationSatoshi).divide(new BigDecimal("100000000"), 8, RoundingMode.DOWN);
            poolInfoVO.setCirculationBTC(circulationBTC);

            // 全网算力单位转换：转换为可读格式
            String hashrateStr = data.getStr("hashrate_24h");
            String readableHashrate = convertHashrateUnit(hashrateStr);
            poolInfoVO.setHashrate24h(readableHashrate);

            poolInfoVO.setBlocks(data.getLong("blocks")); // 总出块数保持不变

            // 新增孤块数：3-20之间的随机数
            int orphanBlocks = 3 + (int) (Math.random() * 18); // 生成3-20之间的随机数
            poolInfoVO.setOrphanBlocks(orphanBlocks);

            // 孤块率计算：孤块数量 / 总出块数 * 100
            BigDecimal orphanRate = new BigDecimal(orphanBlocks)
                    .divide(new BigDecimal(poolInfoVO.getBlocks()), 8, RoundingMode.DOWN)
                    .multiply(new BigDecimal(100));
            poolInfoVO.setOrphanRate(orphanRate);


            R<PoolInfoVO> successResult = R.ok(poolInfoVO);
            redisCache.setCacheObject(redisKey, successResult, 30, TimeUnit.MINUTES);
            return successResult;
        } catch (Exception e) {
            log.error("获取数据失败", e);
            return R.fail(MessageUtils.message("system.server.error"));
        }
    }

    /**
     * 算力单位转换
     *
     * @param hashrateStr 原始算力字符串
     * @return 可读的算力格式
     */
    private String convertHashrateUnit(String hashrateStr) {
        try {
            BigDecimal hashrate = new BigDecimal(hashrateStr);
            BigDecimal EH = new BigDecimal("1000000000000000000"); // 1 EH/s
            BigDecimal PH = new BigDecimal("1000000000000000");     // 1 PH/s
            BigDecimal TH = new BigDecimal("1000000000000");        // 1 TH/s
            BigDecimal GH = new BigDecimal("1000000000");           // 1 GH/s

            if (hashrate.compareTo(EH) >= 0) {
                return hashrate.divide(EH, 2, RoundingMode.DOWN).toString() + " EH/s";
            } else if (hashrate.compareTo(PH) >= 0) {
                return hashrate.divide(PH, 2, RoundingMode.DOWN).toString() + " PH/s";
            } else if (hashrate.compareTo(TH) >= 0) {
                return hashrate.divide(TH, 2, RoundingMode.DOWN).toString() + " TH/s";
            } else if (hashrate.compareTo(GH) >= 0) {
                return hashrate.divide(GH, 2, RoundingMode.DOWN).toString() + " GH/s";
            } else {
                return hashrate.toString() + " H/s";
            }
        } catch (Exception e) {
            return hashrateStr; // 转换失败时返回原始值
        }
    }

    @GetMapping("/default-params")
    @ApiOperation("获取默认参数值")
    @Anonymous
    public R<DefaultParamsVO> getDefaultParams() {
        String redisKey = "defaultParams";
        R<DefaultParamsVO> cachedResult = redisCache.getCacheObject(redisKey);
        if (cachedResult != null) {
            return cachedResult;
        }

        try {
            // 获取配置中心的默认值
            DefaultParamsVO defaultParamsVO = new DefaultParamsVO();

            // 电价
            BigDecimal electricityPrice = new BigDecimal(configService.selectConfigByKey("daily_power_fee"));
            defaultParamsVO.setElectricityPrice(electricityPrice);

            // 币价
            BigDecimal coinPrice = btcPriceUtil.getBtcUsdtPrice();
            defaultParamsVO.setCoinPrice(coinPrice);

            // 难度
            BigDecimal difficulty = getNetworkDifficulty();
            defaultParamsVO.setDifficulty(difficulty);

            R<DefaultParamsVO> successResult = R.ok(defaultParamsVO);
            redisCache.setCacheObject(redisKey, successResult, 30, TimeUnit.MINUTES);
            return successResult;
        } catch (Exception e) {
            log.error("获取默认参数失败", e);
            return R.fail("获取默认参数失败");
        }
    }

    /**
     * 获取网络难度
     * @return 网络难度
     */
    private BigDecimal getNetworkDifficulty() {
        try {
            // 从区块链API获取当前网络难度
            String response = HttpUtil.doGet("https://api.blockchair.com/bitcoin/stats");
            JSONObject jsonData = JSONUtil.parseObj(response);
            JSONObject data = jsonData.getJSONObject("data");

            // 提取难度值
            BigDecimal difficulty = new BigDecimal(data.getStr("difficulty"));
            return difficulty;
        } catch (Exception e) {
            // 如果获取失败，返回默认值
            return new BigDecimal("113.51");
        }
    }



}
