package com.harbor.messageservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.messageservice.domain.po.UserProfilePO;
import com.harbor.messageservice.domain.vo.UserCenterVO;

public interface IUserService extends IService<UserProfilePO> {
    UserCenterVO getUserCenter(Long userId);
}
