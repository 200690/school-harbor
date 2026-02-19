package com.harbor.secondHand.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harbor.secondHand.user.domain.po.User;

public interface UserMapper extends BaseMapper<User> {
//    @Update("update user set balance = balance - ${totalFee} where id = #{userId}")
//    void updateMoney(@Param("userId") Long userId, @Param("totalFee") Integer totalFee);

}