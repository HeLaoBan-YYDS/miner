package com.ruoyi.system.domain;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BizWithdrawCallBackInfo {
    /**
     * 订单号
     */
    private String  trade_id;
    /**
     * 币名
     */
    private String  coin;
    /**
     * 主链币名
     */
    private String  chain;
    /**
     * txid
     */
    private String  txid;
    /**
     * 提现地址
     */
    private String  address;
    /**
     * 订单最后更新时间
     */
    private String  time;
    /**
     * 提现数额，精度统一8位
     */
    private String  amount;
    /**
     * 平台手续费
     */
    private String  fee;
    /**
     * 提现总额 total=amount+fee，精度统一8位
     */
    private String  total;
    /**
     * 提现状态 ： 1 成功；5 出币处理中（此时会推送 txid，但是txid值以最终确认时为准。主要考虑前端展示对用户友好，像btc 类等链交易确认较慢）；10 审核不通过；20 转账失败 ；其他错误码（参考错误码说明）
     */
    private String  status;
    /**
     * 状态描述
     */
    private String  msg;
    /**
     * 订单确认时间
     */
    /**
     * 1站内充值 2链上充值
     */
    private int  type;
}
