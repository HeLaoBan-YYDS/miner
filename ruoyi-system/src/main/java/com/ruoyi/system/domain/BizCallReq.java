package com.ruoyi.system.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BizCallReq {

    private String sign;
    private BizCallBackInfo data;


}
