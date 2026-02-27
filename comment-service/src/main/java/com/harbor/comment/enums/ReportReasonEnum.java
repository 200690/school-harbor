package com.harbor.comment.enums;

/**
 * 举报类型枚举
 */
public enum ReportReasonEnum {
    AD(1, "广告"),
    PORNOGRAPHY(2, "色情"),
    ABUSE(3, "辱骂"),
    FAKE(4, "虚假"),
    OTHER(5, "其他");

    private final Integer code;
    private final String desc;

    ReportReasonEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() { return code; }
    public String getDesc() { return desc; }
}