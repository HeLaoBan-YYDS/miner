package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.ProductType;
import com.ruoyi.common.utils.BtcPriceUtil;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.MinerProject;
import com.ruoyi.system.domain.ShareConfig;
import com.ruoyi.system.domain.dto.CloudInfoDTO;
import com.ruoyi.system.domain.dto.MinerInfoDTO;
import com.ruoyi.system.domain.vo.*;
import com.ruoyi.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目基本信息Controller
 *
 * @author Remi
 * @date 2025-09-16
 */
@RestController
@RequestMapping("/app/project")
@Api(tags = "APP端项目基本信息")
public class AppMinerProjectController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    @Autowired
    private BtcPriceUtil btcPriceUtil;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private IBizOrderService orderService;

    @Autowired
    private IMinerProjectService projectService;


    /**
     * 查询项目基本信息列表
     */
    @GetMapping("/cloud/list")
    @ApiOperation(value = "云算力列表")
    @Anonymous
    public R<List<CloudPowerListVo>> cloudList() {
        String btcPriceStr = configService.selectConfigByKey("btc_usdt_def_price");
        BigDecimal btcPrice = new BigDecimal(btcPriceStr);

        MinerProject minerProjectCondition= new MinerProject();
        minerProjectCondition.setType(ProductType.CLOUD_COMPUTING_POWER.getCode());
        List<MinerProject> minerProjects = projectService.selectMinerProjectList(minerProjectCondition);

        List<CloudPowerListVo> cloudPowerListVos = new ArrayList<>();
        for (MinerProject minerProject : minerProjects) {
            CloudPowerListVo cloudPowerListVo = new CloudPowerListVo();
            BeanUtils.copyBeanProp(cloudPowerListVo, minerProject);
            BigDecimal staticOutputRate = calStaticOutputRate(minerProject, btcPrice);
            cloudPowerListVo.setStaticOutputRate(staticOutputRate);
            cloudPowerListVos.add(cloudPowerListVo);
        }

        return R.ok(cloudPowerListVos);
    }


    /**
     * 矿机列表
     */
    @GetMapping("/miner/list")
    @ApiOperation(value = "矿机列表")
    @Anonymous
    public R<List<MinerListVo>> minerMachineList() {

        String btcPriceStr = configService.selectConfigByKey("btc_usdt_def_price");
        BigDecimal btcPrice = new BigDecimal(btcPriceStr);

        MinerProject minerProjectCondition= new MinerProject();
        minerProjectCondition.setType(ProductType.MINING_MACHINE.getCode());
        List<MinerProject> minerProjects = projectService.selectMinerProjectList(minerProjectCondition);

        // TODO 增加字段 serviceFeeUnitPrice
        ShareConfig shareMode = dictDataService.selectShareMode(132L);
        List<MinerListVo> cloudPowerListVos = new ArrayList<>();
        for (MinerProject minerProject : minerProjects) {
            MinerListVo minerListVo = new MinerListVo();
            BeanUtils.copyBeanProp(minerListVo, minerProject);

            // 默认每日静态产出（美金计价）
            BigDecimal topStaticReturnDays = calMinerTopStaticReturnDays(btcPrice, minerProject.getMinPurchase(), minerProject.getSingleDayStaticOutputBtc(), minerProject.getUnitPrice(), shareMode.getServiceFeeUnitPrice(), 30);
            minerListVo.setTopStaticReturnDays(topStaticReturnDays);
            cloudPowerListVos.add(minerListVo);
        }
        return R.ok(cloudPowerListVos);
    }


    private static BigDecimal calStaticOutputRate(MinerProject minerProject, BigDecimal btcPrice) {
        //静态产出
        BigDecimal staticOutputUSD = minerProject.getSingleDayStaticOutputBtc().multiply(btcPrice).multiply(BigDecimal.valueOf(minerProject.getPlanCycle()));

        //总服务费
        BigDecimal minPurchase = BigDecimal.valueOf(minerProject.getMinPurchase());
        BigDecimal totalServiceFee = calTotalServiceFee(minerProject, minPurchase);

        //期初服务费
        BigDecimal firstServiceFee = BigDecimal.valueOf(minerProject.getMinFirstPaymentDays()).multiply(minPurchase).multiply(minerProject.getServiceFeeUnitPrice());

        //电费
        BigDecimal computingPowerFee = calComputingPowerFee(minerProject.getPlanCycle(), minerProject.getMinPurchase(), minerProject.getUnitPowerCost());

        //期初投入
        BigDecimal firstInvestment = firstServiceFee.add(computingPowerFee);

        //期中投入
        BigDecimal totalInvestment = totalServiceFee.subtract(firstServiceFee);

        //挖矿净得
        BigDecimal netProfit = staticOutputUSD.subtract(totalInvestment);

        //静态收益率
        return netProfit.divide(firstInvestment, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    private static BigDecimal calTotalServiceFee(MinerProject minerProject, BigDecimal minPurchase) {
        return BigDecimal.valueOf(minerProject.getPlanCycle()).multiply(minPurchase).multiply(minerProject.getServiceFeeUnitPrice());
    }


    /**
     * 查询项目基本信息列表
     */
    @GetMapping("/cloud/info")
    @ApiOperation(value = "云算力信息")
    @PreAuthorize("@ss.hasRole('user')")
    public R<CloudPowerPurchaseInfoVO> cloudInfo(CloudInfoDTO dto) {
        MinerProject minerProject = projectService.selectMinerProjectById(dto.getId());
        if (minerProject == null) return R.fail(MessageUtils.message("project.not.exist"));

        String defBtcPriceStr = configService.selectConfigByKey("btc_usdt_def_price");

        String upBtcPriceStr = configService.selectConfigByKey("btc_usdt_up_price");
        BigDecimal defBtcPrice = new BigDecimal(defBtcPriceStr);
        BigDecimal upBtcPrice = new BigDecimal(upBtcPriceStr);

        CloudPowerPurchaseInfoVO vo = new CloudPowerPurchaseInfoVO();
        BeanUtils.copyBeanProp(vo, minerProject);

        innitVO(dto, vo, minerProject, defBtcPrice);

        SysUser user = userService.selectUserById(getLoginUser().getUserId());
        BigDecimal accountUSD = user.getAccount() == null ? BigDecimal.ZERO : user.getAccount();
        vo.setWalletDeductible(accountUSD.negate());

        //算力费用 = 计划周期 * 选择数量 * 算力费单价
        BigDecimal computingPowerFee = calComputingPowerFee(minerProject.getPlanCycle(), vo.getSelectedQuantity(), minerProject.getUnitPowerCost());
        vo.setComputingPowerFee(computingPowerFee);

        vo.setHighestReferencePrice(upBtcPrice);
        vo.setLowestReferencePrice(btcPriceUtil.getBtcUsdtPrice());

        //总服务费
        BigDecimal totalServiceFee = calTotalServiceFee(minerProject, BigDecimal.valueOf(vo.getSelectedQuantity()));
        vo.setMiddleInvestment(totalServiceFee);

        //周期静态产出
        BigDecimal singleDayStaticOutputBTC = minerProject.getSingleDayStaticOutputBtc();
        vo.setStaticOutputBTC(singleDayStaticOutputBTC);

        BigDecimal staticOutputRate = calStaticOutputRate(minerProject, defBtcPrice);
        vo.setTopStaticOutputRate(staticOutputRate);
        return R.ok(vo);
    }

    private static BigDecimal calComputingPowerFee(Integer planCycle, int selectedQuantity, BigDecimal unitPowerCost) {
        return BigDecimal.valueOf(planCycle).multiply(BigDecimal.valueOf(selectedQuantity)).multiply(unitPowerCost);
    }

    private static void innitVO(CloudInfoDTO dto, CloudPowerPurchaseInfoVO vo, MinerProject minerProject, BigDecimal defBtcPrice) {
        if (dto.getSelectedQuantity() != null && dto.getSelectedQuantity() > 0){
            vo.setSelectedQuantity(dto.getSelectedQuantity());
        }else {
            vo.setSelectedQuantity(minerProject.getMinPurchase());
        }

        if (dto.getFirstPaymentDays() != null && dto.getFirstPaymentDays() > 0){
            vo.setFirstPaymentDays(dto.getFirstPaymentDays());
        }else {
            vo.setFirstPaymentDays(minerProject.getMinFirstPaymentDays());
        }

        if (dto.getReferencePrice() != null){
            vo.setReferencePrice(dto.getReferencePrice());
        }else {
            vo.setReferencePrice(defBtcPrice);
        }
    }

    private static void innitVO(MinerInfoDTO dto, MinerInfoVo vo, BigDecimal defBtcPrice) {
        if (dto.getSelectedQuantity() != null && dto.getSelectedQuantity() > 0){
            vo.setSelectedQuantity(dto.getSelectedQuantity());
        }

        if (dto.getFirstPaymentDays() != null && dto.getFirstPaymentDays() > 0){
            vo.setFirstPaymentDays(dto.getFirstPaymentDays());
        }

        if (dto.getUnitStaticOutputBTC() != null){
            vo.setSingleDayStaticOutputBtc(dto.getUnitStaticOutputBTC());
        }

        if (dto.getReferencePrice() != null){
            vo.setReferencePrice(dto.getReferencePrice());
        }else {
            vo.setReferencePrice(defBtcPrice);
        }

        if (dto.getMachineCount() != null && dto.getMachineCount() > 0){
            vo.setMinPurchase(dto.getMachineCount());
        }

        if (dto.getShareModeId() != null && dto.getShareModeId() != 0L){
            vo.setShareModeId(dto.getShareModeId());
        }else {
            vo.setShareModeId(132L);
        }
    }

    /**
     * 矿机列表
     */
    @GetMapping("/miner/info")
    @ApiOperation(value = "矿机信息")
    @PreAuthorize("@ss.hasRole('user')")
    public R<MinerInfoVo> minerMachineInfo(MinerInfoDTO dto) {

        String btcPriceStr = configService.selectConfigByKey("btc_usdt_def_price");
        BigDecimal btcPrice = new BigDecimal(btcPriceStr);

        String upBtcPriceStr = configService.selectConfigByKey("btc_usdt_up_price");
        BigDecimal upBtcPrice = new BigDecimal(upBtcPriceStr);

        MinerProject minerProject = projectService.selectMinerProjectById(dto.getId());
        MinerInfoVo vo = new MinerInfoVo();

        // 默认每日静态产出（美金计价）
        ShareConfig shareMode = dictDataService.selectShareMode(132L);
        BigDecimal topStaticReturnDays = calMinerTopStaticReturnDays(btcPrice, minerProject.getMinPurchase(), minerProject.getSingleDayStaticOutputBtc(), minerProject.getUnitPrice(), shareMode.getServiceFeeUnitPrice(), 30);
        vo.setTopStaticReturnDays(topStaticReturnDays);

        BeanUtils.copyBeanProp(vo, minerProject);

        innitVO(dto, vo, btcPrice);


        List<ShareConfig> shareConfigs = orderService.selectMode();
        vo.setShareModes(shareConfigs);

        SysUser user = userService.selectUserById(getLoginUser().getUserId());
        BigDecimal accountUSD = user.getAccount() == null ? BigDecimal.ZERO : user.getAccount();
        vo.setWalletDeductible(accountUSD.negate());

        vo.setHighestReferencePrice(upBtcPrice);
        vo.setLowestReferencePrice(btcPriceUtil.getBtcUsdtPrice());



        vo.setBottomStaticReturnDays(calMinerTopStaticReturnDays(vo.getReferencePrice(),vo.getMinPurchase(),vo.getSingleDayStaticOutputBtc(),minerProject.getUnitPrice(),shareMode.getServiceFeeUnitPrice(), vo.getMinPurchase()));

        // 矿机残值
        BigDecimal resValue = vo.getResidualValue().multiply(BigDecimal.valueOf(vo.getMinPurchase())).divide(BigDecimal.valueOf(minerProject.getComputePower()), 2, RoundingMode.HALF_DOWN);

        // 静态产出
        BigDecimal staticOutputUSD = vo.getSingleDayStaticOutputBtc().multiply(vo.getReferencePrice());

        // 总静态产出
        BigDecimal totalStaticOutputUSD = staticOutputUSD.multiply(BigDecimal.valueOf(vo.getDepositDays())).multiply(BigDecimal.valueOf(vo.getMinPurchase()));

        // 总服务费
        BigDecimal totalServiceFee = shareMode.getServiceFeeUnitPrice().multiply(BigDecimal.valueOf(vo.getDepositDays())).multiply(BigDecimal.valueOf(vo.getMinPurchase()));

        // 总静态产出 + 矿机残值 - 总服务费
        vo.setEstimatedStaticOutput(totalStaticOutputUSD.add(resValue).subtract(totalServiceFee));

        // 托管费投入金额
        vo.setDepositInvestment(calHostFee(30,BigDecimal.valueOf(vo.getMinPurchase()),shareMode.getServiceFeeUnitPrice()));

        // TODO 增加字段 serviceFeeUnitPrice
        shareMode = dictDataService.selectShareMode(vo.getShareModeId());
        vo.setDeposit(calHostFee(vo.getDepositDays(), BigDecimal.valueOf(vo.getSelectedQuantity()), shareMode.getServiceFeeUnitPrice()));

        return R.ok(vo);
    }

    private static BigDecimal calMinerTopStaticReturnDays(BigDecimal btcPrice, int purchaseQuantity, BigDecimal singleDayStaticOutputBTC, BigDecimal unitPrice, BigDecimal serviceFeeUnitPrice, int hostDays) {
        BigDecimal minPurchase = BigDecimal.valueOf(purchaseQuantity);
        BigDecimal defStaticOutputUSD = calMinerStaticOutPutUSD(btcPrice, minPurchase, singleDayStaticOutputBTC, serviceFeeUnitPrice);

        //默认纯电价模式托管押金=30（天）* 默认选择数量 * 纯电价模式折合服务费单价
        BigDecimal defHostFee = calHostFee(hostDays, minPurchase, serviceFeeUnitPrice);
        //默认机器费 = 机器单价 * 选择数量 + 纯电价模式托管押金
        BigDecimal defMachinePrice = unitPrice.multiply(minPurchase).add(defHostFee);

        //默认静态回本天数 = 机器费/静态产出
        return defMachinePrice.divide(defStaticOutputUSD,2, RoundingMode.HALF_DOWN);
    }

    private static BigDecimal calMinerStaticOutPutUSD(BigDecimal btcPrice, BigDecimal purchaseQuantity, BigDecimal singleDayStaticOutputBTC, BigDecimal serviceFeeUnitPrice) {
        BigDecimal singleDayStaticOutputUSD = singleDayStaticOutputBTC.multiply(btcPrice);
        BigDecimal profit = singleDayStaticOutputUSD.multiply(purchaseQuantity);
        BigDecimal serviceFee = serviceFeeUnitPrice.multiply(purchaseQuantity);
        return profit.subtract(serviceFee);
    }

    private static BigDecimal calHostFee(int days, BigDecimal selectedQuantity, BigDecimal serviceFeeUnitPrice) {
        return new BigDecimal(days).multiply(selectedQuantity).multiply(serviceFeeUnitPrice);
    }


//    /**
//     * 查询项目基本信息列表
//     */
//    @GetMapping("/apr")
//    @ApiOperation("APR")
//    @Anonymous
//    public R<AprInfoVo> apr() {
//        //每日单T产出
//        String dailyYieldPerT = configService.selectConfigByKey("daily_per_t_yield") == null ? "0" : configService.selectConfigByKey("daily_per_t_yield");
//
//        //电费
//        String dailyPowerFee = configService.selectConfigByKey("daily_power_fee") == null ? "0" : configService.selectConfigByKey("daily_power_fee");
//
//        //平台算力
//        String platformTotalComputePower = configService.selectConfigByKey("platform_total_compute_power") == null ? "0" : configService.selectConfigByKey("platform_total_compute_power");
//
//        AprInfoVo aprInfoVo = new AprInfoVo();
//        aprInfoVo.setDailyPowerFee(dailyPowerFee);
//        aprInfoVo.setDailyYieldPerT(dailyYieldPerT);
//        aprInfoVo.setPlatformTotalComputePower(platformTotalComputePower);
//        return R.ok(aprInfoVo);
//    }


}
