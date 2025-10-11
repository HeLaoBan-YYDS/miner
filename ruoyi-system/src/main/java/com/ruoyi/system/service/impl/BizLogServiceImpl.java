package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BizLogMapper;
import com.ruoyi.system.domain.BizLog;
import com.ruoyi.system.service.IBizLogService;

/**
 * 资金流水Service业务层处理
 *
 * @author Remi
 * @date 2025-09-19
 */
@Service
public class BizLogServiceImpl implements IBizLogService
{
    @Autowired
    private BizLogMapper bizLogMapper;

    /**
     * 查询资金流水
     *
     * @param logId 资金流水主键
     * @return 资金流水
     */
    @Override
    public BizLog selectBizLogByLogId(Long logId)
    {
        return bizLogMapper.selectBizLogByLogId(logId);
    }

    /**
     * 查询资金流水列表
     *
     * @param bizLog 资金流水
     * @return 资金流水
     */
    @Override
    public List<BizLog> selectBizLogList(BizLog bizLog)
    {
        return bizLogMapper.selectBizLogList(bizLog);
    }

    /**
     * 新增资金流水
     *
     * @param bizLog 资金流水
     * @return 结果
     */
    @Override
    public int insertBizLog(BizLog bizLog)
    {
        bizLog.setCreateTime(DateUtils.getNowDate());
        return bizLogMapper.insertBizLog(bizLog);
    }

    /**
     * 修改资金流水
     *
     * @param bizLog 资金流水
     * @return 结果
     */
    @Override
    public int updateBizLog(BizLog bizLog)
    {
        return bizLogMapper.updateBizLog(bizLog);
    }

    /**
     * 批量删除资金流水
     *
     * @param logIds 需要删除的资金流水主键
     * @return 结果
     */
    @Override
    public int deleteBizLogByLogIds(Long[] logIds)
    {
        return bizLogMapper.deleteBizLogByLogIds(logIds);
    }

    /**
     * 删除资金流水信息
     *
     * @param logId 资金流水主键
     * @return 结果
     */
    @Override
    public int deleteBizLogByLogId(Long logId)
    {
        return bizLogMapper.deleteBizLogByLogId(logId);
    }

    /**
     * 根据订单号查询资金流水
     */
    @Override
    public BizLog selectBizLogByOrderNo(String orderNo) {
        return bizLogMapper.selectBizLogByOrderNo(orderNo);
    }
}
