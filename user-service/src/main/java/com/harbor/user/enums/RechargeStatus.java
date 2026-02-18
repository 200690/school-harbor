package com.harbor.user.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 充值记录状态枚举
 */
@Getter
public enum RechargeStatus implements IEnum<Integer>  {

    PROCESSING(0, "处理中"),
    SUCCESS(1, "成功"),
    FAILED(2, "失败");

    private final Integer code;
    private final String desc;

    RechargeStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}