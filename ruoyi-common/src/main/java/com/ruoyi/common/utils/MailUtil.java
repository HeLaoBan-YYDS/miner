package com.ruoyi.common.utils;

import com.nxcloud.sdk.NxEmailClient;

import java.util.HashMap;
import java.util.Map;

public class MailUtil {

    public static final String appKey = "KfVPeILm";

    public static final String secretKey = "Cef1ivfL";

    public static String sendMail(String userName, String msg, String templateName) {
        //发送邮件
        NxEmailClient nxEmailClient = new NxEmailClient("http://dapi.nxtele.com/api/email/otp",
                appKey, secretKey);
        Map<String, String> data = new HashMap<>();
        data.put("vCode", msg);
        data.put("userName", userName);
        data.put("templateName",templateName);
        data.put("date",DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.addMinutes(DateUtils.getNowDate(),5)));
        nxEmailClient.param("templateData", data);
        // 此处为传入模板替换值templateData
        Map<String, String> result = null;
        try {
            result = nxEmailClient.send("otp@nxotp.com", userName, templateName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result.get("code");
    }
}
