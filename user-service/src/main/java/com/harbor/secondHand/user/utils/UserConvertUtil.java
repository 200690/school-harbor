package com.harbor.secondHand.user.utils;

import com.harbor.secondHand.user.domain.po.User;
import com.harbor.secondHand.user.domain.vo.UserVO;
import com.harbor.secondHand.user.enums.UserRoleEnum;
import com.harbor.secondHand.user.enums.UserStatusEnum;

/**
 * 用户对象转换工具
 */
public class UserConvertUtil {

    /**
     * PO 转 VO
     */
    public static UserVO toVO(User po) {
        if (po == null) {
            return null;
        }

        UserVO vo = new UserVO();
        vo.setId(po.getId());
        vo.setUsername(po.getUsername());
        vo.setPhone(maskPhone(po.getPhone())); // 手机号脱敏
        vo.setEmail(maskEmail(po.getEmail())); // 邮箱脱敏
        vo.setImg(po.getImg());
        vo.setGender(po.getGender());
        vo.setSignature(po.getSignature());
        vo.setStatusDesc(UserStatusEnum.getDescByValue(po.getStatus()));
        vo.setCreditScore(po.getCreditScore());
        vo.setRoleDesc(UserRoleEnum.getDescByValue(po.getRole()));
        vo.setCreateTime(po.getCreateTime());

        return vo;
    }

    /**
     * 手机号脱敏
     */
    private static String maskPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 邮箱脱敏
     */
    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String name = parts[0];
        if (name.length() <= 3) {
            return "***@" + parts[1];
        }
        return name.substring(0, 3) + "***@" + parts[1];
    }
}