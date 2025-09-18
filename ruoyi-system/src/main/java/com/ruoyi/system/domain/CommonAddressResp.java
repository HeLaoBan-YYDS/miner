package com.ruoyi.system.domain;

import lombok.Data;

import java.util.List;

@Data
public class CommonAddressResp {

    private int status;
    private String msg;
    private String date_time;
    private String time_stamp;
    private List<String> data;
    private String sign;
}
