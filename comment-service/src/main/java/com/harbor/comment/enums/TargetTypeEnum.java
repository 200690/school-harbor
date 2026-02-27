package com.harbor.comment.enums;

/**
 * 业务类型枚举
 */
public enum TargetTypeEnum {
    JOB(0, "兼职"),
    PRODUCT(1, "商品");

    private final Integer code;
    private final String desc;

    TargetTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() { return code; }
    public String getDesc() { return desc; }

    public static String getDescByCode(Integer code) {
        for (TargetTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value.desc;
            }
        }
        return "未知";
    }
}