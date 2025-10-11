package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BizLog;

/**
 * 资金流水Service接口
 *
 * @author Remi
 * @date 2025-09-19
 */
public interface IBizLogService
{
    /**
     * 查询资金流水
     *
     * @param logId 资金流水主键
     * @return 资金流水
     */
    public BizLog selectBizLogByLogId(Long logId);

    /**
     * 根据订单号查询资金流水
     */
    public BizLog selectBizLogByOrderNo(String orderNo);

    /**
     * 查询资金流水列表
     *
     * @param bizLog 资金流水
     * @return 资金流水集合
     */
    public List<BizLog> selectBizLogList(BizLog bizLog);

    /**
     * 新增资金流水
     *
     * @param bizLog 资金流水
     * @return 结果
     */
    public int insertBizLog(BizLog bizLog);

    /**
     * 修改资金流水
     *
     * @param bizLog 资金流水
     * @return 结果
     */
    public int updateBizLog(BizLog bizLog);

    /**
     * 批量删除资金流水
     *
     * @param logIds 需要删除的资金流水主键集合
     * @return 结果
     */
    public int deleteBizLogByLogIds(Long[] logIds);

    /**
     * 删除资金流水信息
     *
     * @param logId 资金流水主键
     * @return 结果
     */
    public int deleteBizLogByLogId(Long logId);
}
