package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.CoinType;
import com.ruoyi.common.enums.LogStatus;
import com.ruoyi.common.enums.LogType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.*;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.dto.WithdrawDTO;
import com.ruoyi.system.mapper.BizLogMapper;
import com.ruoyi.system.service.*;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BizWithdrawServiceImpl implements IBizWithdrawService {


    //热钱包提供的公钥
    @Value("${external.hotWallet.publicKey}")
    public String pubKey;

    //我们自己生成的私钥用于加密参数
    @Value("${external.hotWallet.ownerPriKey}")
    public String ownerPriKey;

    @Autowired
    private BizLogMapper bizLogMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysOperLogService operLogService;

    //热钱包提供的商户ID
    @Value("${external.hotWallet.appId}")
    public String appId;

    @Value("${external.hotWallet.baseUrl}")
    public String baseUrl;

    @Autowired
    private IBizLogService bizLogService;

    @Autowired
    private ISysConfigService configService;


    /**
     * 提现回调
     *
     * @param res
     * @return String
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String withdrawCallback(String res) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        JsonParser jp = new JsonParser();
        JsonObject resEle = jp.parse(res).getAsJsonObject();
        Map<String, Object> params = new HashMap<>();
        JSONObject data = new JSONObject();
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setTitle("提现回调接口日志");
        sysOperLog.setOperParam(res);
        sysOperLog.setStatus(0);
        sysOperLog.setOperTime(new Date());
        sysOperLog.setOperName("Remi");
        try {
            log.info("提现回调:{}", res);
            boolean flag = new SignUtil().verifySign(resEle.get("data"), resEle.get("sign").getAsString(), pubKey);
            log.info("提现回调验签:{}", flag);

            sysOperLog.setRequestMethod(flag ? "true" : "false");

            if (!flag) {
                data.put("success_data", "fail");
                params.put("data", data);
                params.put("status", -200);
                params.put("sign", new SignUtil().genSign(params, ownerPriKey));
                Gson gson = new GsonBuilder().create();
                return gson.toJson(params);
            }

            BizWithdrawCallBackReq bizTransferCallBackReq = JSON.toJavaObject(res, BizWithdrawCallBackReq.class);


            BizLog bizLog = new BizLog();
            bizLog.setOrderNo(bizTransferCallBackReq.getData().getTrade_id());
            List<BizLog> bizLogs = bizLogMapper.selectBizLogList(bizLog);

            if (CollectionUtils.isEmpty(bizLogs) || bizLogs.get(0) == null) {
                throw new RuntimeException("提现记录不存在");
            }

            BizLog withdrawLog = bizLogs.get(0);

            data.put("success_data", "success");
            params.put("data", data);
            params.put("status", 200);
            Gson gson = new GsonBuilder().create();
            params.put("sign", new SignUtil().genSign(params, ownerPriKey));


            //热钱包提现审核不通过
            if ("10".equals(bizTransferCallBackReq.getData().getStatus())) {
                handleNoPass(withdrawLog.getLogId());
                return gson.toJson(params);
            }

            return gson.toJson(params);
        } catch (Exception e) {
            log.error("提现回调失败:", e);
            sysOperLog.setErrorMsg(StringUtils.substring(Convert.toStr(e.getMessage(), ExceptionUtil.getExceptionMessage(e)), 0, 2000));
            sysOperLog.setStatus(1);
            throw new ServiceException(e.getMessage());
        } finally {
            operLogService.insertOperlog(sysOperLog);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handleNoPass(Long id) {
        BizLog withdrawLog = bizLogMapper.selectBizLogByLogId(id);
        withdrawLog.setLogStatus(LogStatus.FAILED.getCode());
        bizLogMapper.updateBizLog(withdrawLog);

        //退款
        BigDecimal returnAmount = withdrawLog.getAmount().negate();
        try {
            userService.updateAccount(withdrawLog.getUserId(), returnAmount, CoinType.getByCode(withdrawLog.getCoinType()).orElse(null));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        //退款日志
        SysUser user = userService.selectUserById(withdrawLog.getUserId());
        withdrawLog.setLogStatus(LogStatus.SUCCESS.getCode());
        withdrawLog.setAmount(returnAmount);
        withdrawLog.setLogType(LogType.REFUND.getCode());
        withdrawLog.setBeforeAmount(user.getAccount());
        bizLogMapper.insertBizLog(withdrawLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdraw(WithdrawDTO withdrawDTO) {
        //提现金额
        BigDecimal withdrawalAmount = withdrawDTO.getAmount();

        //扣减金额,若扣减失败会报错
        Long userId = withdrawDTO.getUserId();
        SysUser user = userService.selectUserById(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw new ServiceException(MessageUtils.message("user.notfound"));
        }
        BigDecimal cashWithdrawalAmount = withdrawalAmount.negate();

        CoinType coinType = CoinType.getByCode(withdrawDTO.getCoin()).orElse(null);
        if (null == coinType){
            throw new ServiceException(MessageUtils.message("coinType.unsupported"));
        }
        try {
            userService.updateAccount(userId, cashWithdrawalAmount,CoinType.getByCode(withdrawDTO.getCoin()).orElse(null));
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        //记录提现日志
        String tradeNo = OrderNoUtils.getOrderNo(LogType.WITHDRAW);
        BizLog bizLog = new BizLog();
        bizLog.setAmount(cashWithdrawalAmount);
        bizLog.setLogType(LogType.WITHDRAW.getCode());
        bizLog.setLogStatus(LogStatus.PENDING.getCode());
        bizLog.setUserId(userId);
        bizLog.setCoinType(CoinType.USDT.getCode());
        bizLog.setOrderNo(tradeNo);
        bizLog.setBeforeAmount(user.getAccount());
        bizLog.setAddress(withdrawDTO.getAddress());
        BigDecimal fee = new BigDecimal(configService.selectConfigByKey(coinType.getFee()));
        bizLog.setFee(fee);
        bizLogService.insertBizLog(bizLog);
    }


    /**
     * 提现审核通过
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePass(Long id) {
        BizLog withdrawLog = bizLogMapper.selectBizLogByLogId(id);

        if (ObjectUtils.isEmpty(withdrawLog)) {
            throw new ServiceException("提现记录不存在");
        }

        String amount = withdrawLog.getAmount().negate().subtract(withdrawLog.getFee()).stripTrailingZeros().toPlainString();

        //审核通过 发送提现申请
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", appId);
        params.put("version", "1.0");
        params.put("key_version", "admin");
        params.put("time", Long.toString(System.currentTimeMillis() / 1000));
        params.put("user_id", withdrawLog.getUserId());
        params.put("coin", CoinType.getCoin(withdrawLog.getCoinType()));
        params.put("address", withdrawLog.getAddress().trim());
        params.put("amount", amount);
        params.put("trade_id", withdrawLog.getOrderNo());
        try {
            params.put("sign", new SignUtil().genSign(params, ownerPriKey));
            Gson gson = new GsonBuilder().create();
            log.info("发起提现申请：{}", gson.toJson(params));
            String res = HttpUtil.doPost(baseUrl + "/transfer", gson.toJson(params));
            if (null == res) {
                throw new ServiceException("提现审核API请求失败");
            }
            log.info("发起提现申请 response:{} ", res);
            JsonParser jp = new JsonParser();
            JsonObject resEle = jp.parse(res).getAsJsonObject();
            boolean retSignOK = new SignUtil().verifySign(resEle, resEle.get("sign").getAsString(), pubKey);
            if (!retSignOK) {
                throw new ServiceException("提现审核API返回数据验签失败");
            }

            String code = resEle.get("status").getAsString();
            if (!code.equals("200")) {
                throw new ServiceException("提现审核API返回数据异常:"+resEle);
            }
            withdrawLog.setLogStatus(LogStatus.SUCCESS.getCode());
            withdrawLog.setTxId(resEle.get("tx_id").getAsString());
            bizLogMapper.updateBizLog(withdrawLog);
        }catch (Exception e) {
            throw new ServiceException("提现审核失败:" + e.getMessage());
        }
    }


}
