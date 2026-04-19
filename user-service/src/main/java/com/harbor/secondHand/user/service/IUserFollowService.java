package com.harbor.secondHand.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.secondHand.user.domain.po.UserFollow;
import com.harbor.secondHand.user.domain.vo.FollowVO;

public interface IUserFollowService extends IService<UserFollow> {
    PageDTO<FollowVO> getMyFollow(PageQuery pageQuery);

    void unfollow(Long followId);

    void follow(Long userId);
}
