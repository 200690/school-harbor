package com.harbor.reportservice.enums;

public enum ReportTypeEnum {
    COMMENT(1, "评论"),
    ITEM(2, "商品"),
    PART_TIME(3, "兼职"),
    USER(4, "用户");

    private final Integer code;
    private final String desc;

    ReportTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(Integer code) {
        if (code == null) return null;
        for (ReportTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}
