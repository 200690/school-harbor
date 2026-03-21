package com.harbor.reportservice.enums;

public enum HandleActionEnum {
    DELETE_CONTENT(1, "删除内容"),
    WARN_USER(2, "警告用户"),
    BAN_USER(3, "封禁用户"),
    IGNORE(4, "忽略");

    private final Integer code;
    private final String desc;

    HandleActionEnum(Integer code, String desc) {
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
        for (HandleActionEnum e : values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }
}
