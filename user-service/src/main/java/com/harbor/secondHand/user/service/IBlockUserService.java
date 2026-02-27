package com.harbor.secondHand.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.secondHand.user.domain.po.UserBlockUser;

public interface IBlockUserService extends IService<UserBlockUser> {
    PageDTO getMyBlockUsers(PageQuery pageQuery);

    void unblock(Long blockUserId);
}
