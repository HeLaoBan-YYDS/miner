package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.CoinType;
import com.ruoyi.common.enums.LogStatus;
import com.ruoyi.common.enums.LogType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.*;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.IBizLogService;
import com.ruoyi.system.service.ISysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BizAddressMapper;
import com.ruoyi.system.service.IBizAddressService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * 充值地址管理Service业务层处理
 *
 * @author Remi
 * @date 2025-09-17
 */
@Service
@Slf4j
public class BizAddressServiceImpl implements IBizAddressService {
    @Autowired
    private BizAddressMapper bizAddressMapper;

    public static final String syncAddrsssStatusUrl = "/address/syncStatus";

    public static final String getAddressBatchUrl = "/address/getBatch";

    //热钱包提供的公钥
    @Value("${external.hotWallet.publicKey}")
    public String pubKey;

    //热钱包提供的风控公钥
    @Value("${external.hotWallet.fenkongPubKey}")
    public String fenkongPubKey;

    //热钱包提供的商户ID
    @Value("${external.hotWallet.appId}")
    public String appId;

    //我们自己生成的私钥用于加密参数
    @Value("${external.hotWallet.ownerPriKey}")
    public String ownerPriKey;

    @Value("${external.hotWallet.ownerPubKey}")
    public String ownerPubKey;

    @Value("${external.hotWallet.baseUrl}")
    public String baseUrl;

    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private IBizLogService bizLogService;

    /**
     * 查询充值地址管理
     *
     * @param id 充值地址管理主键
     * @return 充值地址管理
     */
    @Override
    public BizAddress selectBizAddressById(Long id) {
        return bizAddressMapper.selectBizAddressById(id);
    }

    /**
     * 查询充值地址管理列表
     *
     * @param bizAddress 充值地址管理
     * @return 充值地址管理
     */
    @Override
    public List<BizAddress> selectBizAddressList(BizAddress bizAddress) {
        return bizAddressMapper.selectBizAddressList(bizAddress);
    }

    /**
     * 新增充值地址管理
     *
     * @param bizAddress 充值地址管理
     * @return 结果
     */
    @Override
    public int insertBizAddress(BizAddress bizAddress) {
        bizAddress.setCreateTime(DateUtils.getNowDate());
        return bizAddressMapper.insertBizAddress(bizAddress);
    }

    /**
     * 修改充值地址管理
     *
     * @param bizAddress 充值地址管理
     * @return 结果
     */
    @Override
    public int updateBizAddress(BizAddress bizAddress) {
        bizAddress.setUpdateTime(DateUtils.getNowDate());
        return bizAddressMapper.updateBizAddress(bizAddress);
    }

    /**
     * 批量删除充值地址管理
     *
     * @param ids 需要删除的充值地址管理主键
     * @return 结果
     */
    @Override
    public int deleteBizAddressByIds(Long[] ids) {
        return bizAddressMapper.deleteBizAddressByIds(ids);
    }

    /**
     * 删除充值地址管理信息
     *
     * @param id 充值地址管理主键
     * @return 结果
     */
    @Override
    public int deleteBizAddressById(Long id) {
        return bizAddressMapper.deleteBizAddressById(id);
    }

    /**
     * 获取当前人的充值地址
     *
     * @param loginUser 当前登录用户
     * @param coinType  币种
     * @return 地址
     */
    @Override
    public String getAddress(LoginUser loginUser, String coinType) {
        BizAddress bizAddressCondition = new BizAddress();
        bizAddressCondition.setUserId(loginUser.getUserId());
        bizAddressCondition.setCoinType(coinType);
        //查询当前用户是否已经有地址
        List<BizAddress> bizAddresses = bizAddressMapper.selectBizAddressList(bizAddressCondition);
        if (!CollectionUtils.isEmpty(bizAddresses)) {
            //已经存在地址 直接返回
            return bizAddresses.get(0).getAddress();
        }
        bizAddressCondition.setUserId(null);
        bizAddressCondition.setUseFlag("0");
        //还没有地址 从数据库获取未使用的地址 进行分配
        List<BizAddress> notUseAddressList = bizAddressMapper.selectBizAddressList(bizAddressCondition);
        //还有未使用的address 分配
        if (!CollectionUtils.isEmpty(notUseAddressList)) {
            //同步使用状态
            BizAddress notUseAddress = notUseAddressList.get(0);
            boolean sycFlag = this.syncStatus(notUseAddress.getAddress(), String.valueOf(loginUser.getUserId()), coinType);
            if (!sycFlag) {
                return null;
            }
            notUseAddress.setUserId(loginUser.getUserId());
            notUseAddress.setUseFlag("1");
            //更新地址使用状态
            bizAddressMapper.updateBizAddress(notUseAddress);
            return notUseAddress.getAddress();
        }


        //如果地址已经使用完 则从链上获取
        List<String> addresses = this.getRemoteAddress(coinType);
        //获取地址失败了
        if (CollUtil.isEmpty(addresses)) {
            throw new ServiceException(MessageUtils.message("get.address.fail"));
        }
        String address = addresses.get(0);
        //同步状态
        boolean sycFlag = this.syncStatus(address, String.valueOf(loginUser.getUserId()), coinType);
        if (!sycFlag) {
            return null;
        }
        BizAddress usebizAddress = new BizAddress();
        usebizAddress.setUseFlag("1");
        usebizAddress.setAddress(address);
        usebizAddress.setUserId(loginUser.getUserId());
        usebizAddress.setCoinType(coinType);
        //更新使用
        bizAddressMapper.insertBizAddress(usebizAddress);
        addresses.remove(address);

        BizAddress noUsebizAddress = new BizAddress();
        noUsebizAddress.setUserId(loginUser.getUserId());
        noUsebizAddress.setCoinType(coinType);
        noUsebizAddress.setUseFlag("0");
        if (!CollectionUtils.isEmpty(addresses)) {
            addresses.forEach(addressNew -> {
                noUsebizAddress.setAddress(addressNew);
                bizAddressMapper.insertBizAddress(noUsebizAddress);
            });
        }
        return address;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String callBack(String res) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        JsonParser jp = new JsonParser();
        JsonObject resEle = jp.parse(res).getAsJsonObject();
        Map<String, Object> params = new HashMap<>();
        JSONObject data = new JSONObject();
        try {
            boolean flag = new SignUtil().verifySign(resEle.get("data"), resEle.get("sign").getAsString(), pubKey);
            log.info("充值回调验签:{}", flag);
            Gson gson = new GsonBuilder().create();
            if (flag) {
                data.put("success_data", "success");
                params.put("sign", new SignUtil().genSign(params, ownerPriKey));
                params.put("data", data);
                params.put("status", 200);
                BizCallReq bizCallReq = JSON.toJavaObject(res, BizCallReq.class);
                this.insertInfoAndUpdateAccount(bizCallReq);
            } else {
                data.put("success_data", "fail");
                params.put("sign", new SignUtil().genSign(params, ownerPriKey));
                params.put("data", data);
                params.put("status", -200);
            }
            String response = gson.toJson(params);
            saveSuccessLog(res,response);
            return response;
        } catch (Exception e) {
            log.error("充值回调异常", e);
            saveErrorLog(res, e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public String riskCallback(String res) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        JsonParser jp = new JsonParser();
        JsonObject resEle = jp.parse(res).getAsJsonObject();
        Map<String, Object> params = new HashMap<>();
        JSONObject data = new JSONObject();
        Map<String, String> jsonObject = new SignUtil().toStringMap(resEle.get("data"));
        data.put("timestamp", System.currentTimeMillis() / 1000);
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setTitle("风控回调");
        sysOperLog.setOperParam(res);
        sysOperLog.setStatus(0);
        sysOperLog.setOperTime(new Date());
        sysOperLog.setOperName("Remi");
        try {
            log.info("提现风控回调:{}", res);
            boolean flag = new SignUtil().verifySign(resEle.get("data"), resEle.get("sign").getAsString(), fenkongPubKey);
            log.info("提现风控回调验签:{}", flag);
            if (!flag) {
                //验签不通过
                data.put("status_code", 5400);
                data.put("order_id", jsonObject.get("order_id"));
                params.put("sign", new SignUtil().genSign(data, ownerPriKey));
                params.put("data", data);
                params.put("status", 5400);
                Gson gson = new GsonBuilder().create();
                sysOperLog.setStatus(1);
                sysOperLog.setErrorMsg("风控回调验签失败");
                return gson.toJson(params);
            }
            //获取参数 和 数据库数据进行比对
            log.info("提现风控回调 order Id:{}", jsonObject.get("order_id"));
            //获取交易ID 查询 提现记录数据
            BizLog bizLog = bizLogService.selectBizLogByOrderNo(jsonObject.get("order_id"));
            if (null == bizLog) {
                //不存在
                log.info("提现风控回调 订单不存在-----");
                data.put("status_code", 5400);
                data.put("order_id", jsonObject.get("order_id"));
                params.put("sign", new SignUtil().genSign(data, ownerPriKey));
                params.put("data", data);
                params.put("status", 5400);
                Gson gson = new GsonBuilder().create();
                sysOperLog.setStatus(1);
                sysOperLog.setErrorMsg("订单不存在");
                return gson.toJson(params);
            }
            //订单存在  需要对比 金额 地址等是否 一样
            int code = this.comparamInfo(bizLog, jsonObject);
            log.info("提现风控回调 数据比对 code:{}", code);
            data.put("status_code", code);
            data.put("order_id", jsonObject.get("order_id"));
            Gson gson = new GsonBuilder().create();
            params.put("sign", new SignUtil().genSign(data, ownerPriKey));
            params.put("data", data);
            params.put("status", code);
            log.info("风控提现返回：{}", gson.toJson(params));
            sysOperLog.setJsonResult("风控结果:"+gson.toJson(params));
            return gson.toJson(params);
        } catch (Exception e) {
            data.put("status_code", 5007);
            data.put("order_id", jsonObject.get("order_id"));
            params.put("sign", new SignUtil().genSign(data, ownerPriKey));
            params.put("data", data);
            params.put("status", 5007);
            Gson gson = new GsonBuilder().create();
            sysOperLog.setStatus(1);
            sysOperLog.setErrorMsg(StringUtils.substring(Convert.toStr(e.getMessage(), ExceptionUtil.getExceptionMessage(e)), 0, 2000));
            return gson.toJson(params);
        } finally {
            operLogService.insertOperlog(sysOperLog);
        }
    }


    /**
     * 对比
     *
     * @param bizLog
     * @param jsonObject
     * @return
     */
    private int comparamInfo(BizLog bizLog, Map<String, String> jsonObject) {
        //默认是审核通过
        int code = 200;
        //地址不一致
        if (!bizLog.getAddress().trim().equals(String.valueOf(jsonObject.get("address")))) {
            code = 5004;
        }
        return code;
    }


    /**
     * 保存充值记录
     *
     * @param bizCallReq 充值回调请求参数
     */
    private void insertInfoAndUpdateAccount(BizCallReq bizCallReq) {
        BizCallBackInfo data = bizCallReq.getData();
        BizAddress bizAddress = new BizAddress();
        bizAddress.setAddress(data.getAddress());
        List<BizAddress> bizAddresses = bizAddressMapper.selectBizAddressList(bizAddress);
        if (CollectionUtils.isEmpty(bizAddresses) || bizAddresses.get(0) == null || ObjectUtils.isEmpty(bizAddresses.get(0).getUserId())) {
            throw new RuntimeException("充值地址不存在:" + JSON.toJSONString(bizCallReq));
        }
        BizAddress aiuAddress = bizAddresses.get(0);

        SysUser user = sysUserMapper.selectUserById(aiuAddress.getUserId());
        if (ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(user.getUserId())) {
            throw new RuntimeException("充值用户不存在:" + JSON.toJSONString(bizCallReq));
        }

        //更新前的账户
        BigDecimal beforeAccount = user.getAccount();
        BigDecimal topUpAmount = new BigDecimal(data.getTotal());

        if (topUpAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("充值金额必须大于0:" + JSON.toJSONString(bizCallReq));
        }

        int row = sysUserMapper.updateAccount(user.getUserId(), topUpAmount);
        if (row < 1) {
            throw new RuntimeException("更新用户账户失败:" + JSON.toJSONString(bizCallReq));
        }

        // 记录充值日志
        BizLog bizLog = new BizLog();
        bizLog.setAmount(topUpAmount);
        bizLog.setBeforeAmount(beforeAccount);
        bizLog.setUserId(user.getUserId());
        bizLog.setFromUserId(user.getUserId());
        bizLog.setToUserId(user.getUserId());
        bizLog.setOrderNo(data.getOrder_id());
        bizLog.setCoinType(CoinType.USDT.getCode());
        bizLog.setLogType(LogType.RECHARGE.getCode());
        bizLog.setLogStatus(LogStatus.SUCCESS.getCode());
        bizLog.setTxId(data.getTxid());
        bizLogService.insertBizLog(bizLog);
    }


    private void saveErrorLog(String res, Exception e) {
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setTitle("充值回调异常");
        sysOperLog.setOperParam(res);
        sysOperLog.setErrorMsg(StringUtils.substring(Convert.toStr(e.getMessage(), ExceptionUtil.getExceptionMessage(e)), 0, 2000));
        operLogService.insertOperlog(sysOperLog);
    }



    private void saveSuccessLog(String res, String req) {
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setTitle("充值回调成功");
        sysOperLog.setOperParam(res);
        sysOperLog.setJsonResult("充值回调结果"+req);
        sysOperLog.setStatus(0);
        operLogService.insertOperlog(sysOperLog);
    }



    /**
     * 同步地址使用状态
     *
     * @param address
     * @param userId
     * @return
     */
    public boolean syncStatus(String address, String userId, String coinType) {
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", appId);
        params.put("version", "1.0");
        params.put("key_version", "admin");
        params.put("time", Long.toString(System.currentTimeMillis() / 1000));
        params.put("coin", coinType);
        params.put("address", address);
        params.put("user_id", userId);
        try {
            params.put("sign", new SignUtil().genSign(params, ownerPriKey));
            Gson gson = new GsonBuilder().create();
            log.info("params = {}", gson.toJson(params));
            String res = HttpUtil.doPost(baseUrl + syncAddrsssStatusUrl, gson.toJson(params));
            if (null == res) {
                throw new RuntimeException("同步充值地址使用状态接口请求失败");
            }
            log.info("response = {}", res);
            JsonParser jp = new JsonParser();
            JsonObject resEle = jp.parse(res).getAsJsonObject();
            boolean retSignOK = new SignUtil().verifySign(resEle, resEle.get("sign").getAsString(), pubKey);
            if (!retSignOK) {
                throw new RuntimeException("充值地址使用状态同步返回结果验签失败");
            }
            return true;
        } catch (Exception e) {
            log.error("充值地址使用状态同步失败:", e);
            return false;
        }
    }


    /**
     * 从远程获取地址
     *
     * @return
     */
    private List<String> getRemoteAddress(String coinType) {
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", appId);
        params.put("version", "1.0");
        params.put("key_version", "admin");
        params.put("time", Long.toString(System.currentTimeMillis() / 1000));
        params.put("coin", coinType);
        try {
            params.put("sign", new SignUtil().genSign(params, ownerPriKey));
            Gson gson = new GsonBuilder().create();
            JsonParser jp = new JsonParser();
            log.info("获取热钱包地址params = {}", gson.toJson(params));
            String res = HttpUtil.doPost(baseUrl + getAddressBatchUrl, gson.toJson(params));
            if (null == res) {
                throw new RuntimeException("获取充值地址接口请求失败");
            }
            log.info("获获取热钱包地址response = {}", res);
            JsonObject resEle1 = jp.parse(res).getAsJsonObject();
            boolean retSignOK = new SignUtil().verifySign(resEle1, resEle1.get("sign").getAsString(), pubKey);
            if (!retSignOK) {
                throw new RuntimeException("获取充值地址返回结果验签失败");
            }
            CommonAddressResp commonAddressResp = JSON.toJavaObject(res, CommonAddressResp.class);
            return commonAddressResp.getData();
        } catch (Exception e) {
            log.error("获取充值地址失败:", e);
            return null;
        }
    }
}
