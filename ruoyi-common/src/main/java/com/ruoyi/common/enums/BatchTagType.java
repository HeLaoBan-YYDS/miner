package com.ruoyi.common.enums;

public enum BatchTagType {
    THE_FIRST_BATCH_OF_01("THE_FIRST_BATCH_OF_01", "第一期01批"),
    THE_FIRST_BATCH_OF_02("THE_FIRST_BATCH_OF_02", "第一期02批"),
    THE_FIRST_BATCH_OF_03("THE_FIRST_BATCH_OF_03", "第一期03批"),
    THE_SECOND_PHASE_OF_BATCH_01("THE_SECOND_PHASE_OF_BATCH_01", "第二期01批"),
    THE_SECOND_PHASE_OF_BATCH_02("THE_SECOND_PHASE_OF_BATCH_02", "第二期02批"),
    THE_SECOND_PHASE_OF_BATCH_03("THE_SECOND_PHASE_OF_BATCH_03", "第二期03批"),
    THE_THIRD_PHASE_OF_BATCH_01("THE_THIRD_PHASE_OF_BATCH_01", "第三期01批"),
    THE_THIRD_PHASE_OF_BATCH_02("THE_THIRD_PHASE_OF_BATCH_02", "第三期02批"),
    THE_THIRD_PHASE_OF_BATCH_03("THE_THIRD_PHASE_OF_BATCH_03", "第三期03批");

    private final String code;
    private final String info;

    BatchTagType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
