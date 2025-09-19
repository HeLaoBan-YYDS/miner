package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.WithdrawDTO;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public interface IBizWithdrawService {

    /**
     * 提现回调
     * @param data 回调参数
     * @return
     */
    String withdrawCallback(String data) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException;



    void handleNoPass(Long id);

    /**
     * 提现
     *
     * @param withdrawDTO 提现参数
     */
    void withdraw(WithdrawDTO withdrawDTO);

    /**
     * 提现审核通过
     *
     * @param id
     */
    void handlePass(Long id);

}
