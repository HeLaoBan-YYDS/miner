package com.ruoyi.system.domain;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BizCallBackInfo {
    /**
     * 订单号
     */
    private String  order_id;
    /**
     * 币名
     */
    private String  coin;
    /**
     * 主链币名
     */
    private String  chain;
    /**
     * 入账地址
     */
    private String  address;
    /**
     * 转出地址，正式环境2023-05-05之后接入的商户才会返回该属性
     */
    private String  from_address;
    /**
     * 链上txid
     */
    private String  txid;
    /**
     * 实际到账 total = amount - fee
     */
    private String  total;
    /**
     * 平台手续费
     */
    private String  fee;
    /**
     * 链上金额（本字段仅供参考）
     */
    private String  amount;
    /**
     * 0入账但未确认 1确认成功 5失败 8等待退款 10退款中 20已退款
     */
    private int  status;
    /**
     * 确认数仅供参考，以status=1为入账成功
     */
    private int  confirm_count;
    /**
     * 订单确认时间
     */
    private int  time;
    /**
     * 1站内充值 2链上充值
     */
    private int  type;
    /**
     * 商户审核时选择的拒绝原因
     */
    private String  reject_reason;
}
