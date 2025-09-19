package com.ruoyi.system.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BizWithdrawCallBackReq {

    private String sign;

    private BizWithdrawCallBackInfo data;
}
