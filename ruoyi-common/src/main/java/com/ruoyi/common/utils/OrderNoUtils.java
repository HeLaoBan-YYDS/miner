package com.ruoyi.common.utils;

import com.ruoyi.common.enums.LogType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNoUtils {
    public static final String PREFIX = "AIU";

    public static String getOrderNo(LogType logType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = simpleDateFormat.format(new Date());
        return logType.getCode() + timestamp + (int) (Math.random() * 900 + 100);
    }
}
