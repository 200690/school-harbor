package com.harbor.messageservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harbor.messageservice.domain.po.UserProfilePO;

public interface UserMapper extends BaseMapper<UserProfilePO> {
  UserProfilePO selectByUserId(Long userId);

  void deleteByUserId(Long userId);
}
