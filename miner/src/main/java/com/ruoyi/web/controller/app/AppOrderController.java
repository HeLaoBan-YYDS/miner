package com.ruoyi.web.controller.app;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.LogType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BizLog;
import com.ruoyi.system.domain.BizOrder;
import com.ruoyi.system.domain.ShareConfig;
import com.ruoyi.system.domain.dto.PlaceDTO;
import com.ruoyi.system.domain.vo.BizLogVo;
import com.ruoyi.system.service.IBizLogService;
import com.ruoyi.system.service.IBizOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/order")
@Api(tags = "app订单管理")
public class AppOrderController extends BaseController {

    @Autowired
    private IBizOrderService orderService;

    @Autowired
    private IBizLogService bizLogService;


    @PostMapping("/place")
    @PreAuthorize("@ss.hasRole('user')")
    @Log(title = "下单", businessType = BusinessType.UPDATE)
    @ApiOperation("下单")
    @RepeatSubmit(message = "repeat.submit.message")
    public AjaxResult place(@RequestBody @Valid PlaceDTO placeDTO) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Assert.notNull(loginUser, MessageUtils.message("user.notfound"));
        placeDTO.setUserId(loginUser.getUserId());
        orderService.place(placeDTO);
        return success();
    }



    /**
     * 查询项目基本信息列表
     */
    @GetMapping("/list")
    @ApiOperation("我的订单列表-记得分页参数")
    @Anonymous
    public TableDataInfo<List<BizOrder>> list()
    {
        BizOrder bizOrder = new BizOrder();
        startPage();
        bizOrder.setUserId(getUserId());
        List<BizOrder> list = orderService.selectBizOrderList(bizOrder);
        for (BizOrder order : list) {
            order.setProjectName(DictUtils.getDictLabel("project_dict",order.getProjectId().toString()));
            //获得回本进度
            BigDecimal orderFee = orderService.getFeeUSDTByOrderId(order.getOrderId());
            BigDecimal orderIncome = orderService.getIncomeUSDTByOrderId(order.getOrderId());
            BigDecimal backProgress = orderIncome.subtract(orderFee).divide(order.getPaymentAmount(),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            order.setBackProgress(backProgress);
        }
        return getDataTable(list);
    }


    /**
     * 查询项目基本信息列表
     */
    @PreAuthorize("@ss.hasRole('user')")
    @GetMapping("income/list")
    @ApiOperation("我的收益列表-记得分页参数")
    public TableDataInfo<List<BizLog>> incomeList()
    {
        BizLog bizLog = new BizLog();
        startPage();
        bizLog.setUserId(getUserId());
        bizLog.setLogType(LogType.INCOME.getCode());
        List<BizLog> list = bizLogService.selectBizLogList(bizLog);

        BizOrder bizOrder = new BizOrder();
        bizOrder.setUserId(getUserId());
        List<BizOrder> orderList = orderService.selectBizOrderList(bizOrder);
        if (CollUtil.isEmpty(orderList)){
            return getDataTable(list);
        }
        Map<String, BizOrder> orderMap = orderList.stream().collect(Collectors.toMap(BizOrder::getOrderId, p -> p));

        List<BizLogVo> bizLogVoList = new ArrayList<>();
        for (BizLog log : list) {
            BizLogVo bizLogVo = new BizLogVo();
            BizOrder order = orderMap.get(log.getOrderNo());
            if (order == null){
                throw new ServiceException("订单不存在");
            }
            bizLogVo.setProjectName(DictUtils.getDictLabel("project_dict",order.getProjectId().toString()));
            bizLogVo.setCreateTime(log.getCreateTime());
            bizLogVo.setOrderComputePower(order.getComputePower());
            bizLogVo.setIncomeAmount(log.getAmount());
            bizLogVo.setOrderId(log.getOrderNo());
            bizLogVo.setDailyPowerFee(log.getDailyPowerFee());
            bizLogVoList.add(bizLogVo);
        }

        return getDataTable(bizLogVoList);
    }


    @GetMapping("/mode")
    @ApiOperation("获取分红模式")
    @Anonymous
    public R<List<ShareConfig>> mode() {
        List<ShareConfig> shareConfigs = orderService.selectMode();
        return R.ok(shareConfigs);
    }
}
