package com.harbor.partTime.enums;

/**
 * 兼职状态枚举
 */
public enum PartTimeStatusEnum {
    OFF(0, "已下架"),
    RECRUITING(1, "招聘中"),
    FULL(2, "已招满"),
    EXPIRED(3, "已截止");

    private final Integer value;
    private final String desc;

    PartTimeStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByValue(Integer value) {
        if (value == null) return null;
        for (PartTimeStatusEnum status : values()) {
            if (status.value.equals(value)) {
                return status.desc;
            }
        }
        return "未知状态";
    }
}