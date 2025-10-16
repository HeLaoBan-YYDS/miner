package com.ruoyi.system.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.system.domain.BizOrder;
import com.ruoyi.system.domain.ShareConfig;
import com.ruoyi.system.domain.dto.PlaceDTO;

/**
 * 订单Service接口
 *
 * @author Remi
 * @date 2025-09-16
 */
public interface IBizOrderService
{
    /**
     * 查询订单
     *
     * @param orderId 订单主键
     * @return 订单
     */
    public BizOrder selectBizOrderByOrderId(Long orderId);

    /**
     * 查询订单列表
     *
     * @param bizOrder 订单
     * @return 订单集合
     */
    public List<BizOrder> selectBizOrderList(BizOrder bizOrder);

    /**
     * 新增订单
     *
     * @param bizOrder 订单
     * @return 结果
     */
    public int insertBizOrder(BizOrder bizOrder);

    /**
     * 修改订单
     *
     * @param bizOrder 订单
     * @return 结果
     */
    public int updateBizOrder(BizOrder bizOrder);

    /**
     * 批量删除订单
     *
     * @param orderIds 需要删除的订单主键集合
     * @return 结果
     */
    public int deleteBizOrderByOrderIds(Long[] orderIds);

    /**
     * 删除订单信息
     *
     * @param orderId 订单主键
     * @return 结果
     */
    public int deleteBizOrderByOrderId(Long orderId);

    /**
     * 下单
     * @param placeDTO  下单信息
     */
    void place(PlaceDTO placeDTO);

    /**
     * 获取昨日收益
     * @param userId 用户ID
     * @return
     */
    public BigDecimal getYesterdayIncome(Long userId);

    /**
     * 获取总收益
     * @param userId 用户ID
     * @return
     */
    public BigDecimal getTotalIncome(Long userId);

    BigDecimal getIncomeByOrderId(String orderId);

    /**
     * 获取分红模式
     * @return 分红模式
     */
    List<ShareConfig> selectMode();
}
