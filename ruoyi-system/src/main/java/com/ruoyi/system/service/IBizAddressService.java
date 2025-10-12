package com.ruoyi.system.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.system.domain.BizAddress;

/**
 * 充值地址管理Service接口
 *
 * @author Remi
 * @date 2025-09-17
 */
public interface IBizAddressService
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
     * 批量删除充值地址管理
     *
     * @param ids 需要删除的充值地址管理主键集合
     * @return 结果
     */
    public int deleteBizAddressByIds(Long[] ids);

    /**
     * 删除充值地址管理信息
     *
     * @param id 充值地址管理主键
     * @return 结果
     */
    public int deleteBizAddressById(Long id);

    /**
     * 获取用户充值地址
     * @param loginUser
     * @param coinType
     * @return
     */
    String getAddress(LoginUser loginUser, String coinType);


    /**
     * 充值回调
     * @param data 回调数据
     * @return 结果
     */
    String callBack(String data) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException;

    /**
     * 风控回调
     * @param data
     * @return
     */
    String riskCallback(String data) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException;
}
