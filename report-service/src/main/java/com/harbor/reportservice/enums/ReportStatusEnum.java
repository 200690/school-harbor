package com.harbor.reportservice.enums;

public enum ReportStatusEnum {
    PENDING(0, "待处理"),
    HANDLED_VIOLATION(1, "已处理（违规）"),
    HANDLED_NORMAL(2, "已处理（正常）");

    private final Integer code;
    private final String desc;

    ReportStatusEnum(Integer code, String desc) {
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
        for (ReportStatusEnum e : values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}
