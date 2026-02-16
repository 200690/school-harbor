package com.harbor.user.enums;

import lombok.Getter;

/**
 * 充值记录状态枚举
 */
@Getter
public enum RechargeStatus {

    PROCESSING(0, "处理中"),
    SUCCESS(1, "成功"),
    FAILED(2, "失败");

    private final Integer code;
    private final String desc;

    RechargeStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RechargeStatus fromCode(Integer code) {
        for (RechargeStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知状态码: " + code);
    }
}