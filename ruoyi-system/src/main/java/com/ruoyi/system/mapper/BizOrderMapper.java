package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizOrder;

/**
 * 订单Mapper接口
 *
 * @author Remi
 * @date 2025-09-16
 */
public interface BizOrderMapper
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
     * 删除订单
     *
     * @param orderId 订单主键
     * @return 结果
     */
    public int deleteBizOrderByOrderId(Long orderId);

    /**
     * 批量删除订单
     *
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizOrderByOrderIds(Long[] orderIds);
}
