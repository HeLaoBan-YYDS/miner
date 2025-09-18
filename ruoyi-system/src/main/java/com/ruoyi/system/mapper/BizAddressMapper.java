package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BizAddress;

/**
 * 充值地址管理Mapper接口
 *
 * @author Remi
 * @date 2025-09-17
 */
public interface BizAddressMapper
{
    /**
     * 查询充值地址管理
     *
     * @param id 充值地址管理主键
     * @return 充值地址管理
     */
    public BizAddress selectBizAddressById(Long id);

    /**
     * 查询充值地址管理列表
     *
     * @param bizAddress 充值地址管理
     * @return 充值地址管理集合
     */
    public List<BizAddress> selectBizAddressList(BizAddress bizAddress);

    /**
     * 新增充值地址管理
     *
     * @param bizAddress 充值地址管理
     * @return 结果
     */
    public int insertBizAddress(BizAddress bizAddress);

    /**
     * 修改充值地址管理
     *
     * @param bizAddress 充值地址管理
     * @return 结果
     */
    public int updateBizAddress(BizAddress bizAddress);

    /**
     * 删除充值地址管理
     *
     * @param id 充值地址管理主键
     * @return 结果
     */
    public int deleteBizAddressById(Long id);

    /**
     * 批量删除充值地址管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizAddressByIds(Long[] ids);
}
