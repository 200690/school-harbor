package com.harbor.partTime.enums;

import lombok.Getter;

/**
 * 兼职类型枚举
 */
@Getter
public enum PartTimeTypeEnum {
    CAMPUS(1, "校内兼职"),
    OFF_CAMPUS(2, "校外兼职"),
    INTERNSHIP(3, "实习");

    private final Integer value;
    private final String desc;

    PartTimeTypeEnum(Integer value, String desc) {
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
        for (PartTimeTypeEnum type : values()) {
            if (type.value.equals(value)) {
                return type.desc;
            }
        }
        return "未知类型";
    }
}