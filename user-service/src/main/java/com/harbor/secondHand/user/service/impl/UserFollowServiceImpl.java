package com.harbor.secondHand.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.utils.UserContext;
import com.harbor.secondHand.user.domain.po.User;
import com.harbor.secondHand.user.domain.po.UserFollow;
import com.harbor.secondHand.user.domain.vo.FollowVO;
import com.harbor.secondHand.user.mapper.UserFollowMapper;
import com.harbor.secondHand.user.mapper.UserMapper;
import com.harbor.secondHand.user.service.IUserFollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements IUserFollowService {
    private final UserMapper userMapper;

    @Override
    public PageDTO<FollowVO> getMyFollow(PageQuery pageQuery) {
        Page<UserFollow> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        LambdaQueryWrapper<UserFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFollow::getUserId, UserContext.getUser());
        wrapper.eq(UserFollow::getIsDelete, 0);
        wrapper.orderByDesc(UserFollow::getCreateTime);

        Page<UserFollow> userFollowPage = this.page(page, wrapper);

        List<FollowVO> followVOS = userFollowPage.getRecords().stream().map(userFollow -> {
            FollowVO followVO = new FollowVO();
            followVO.setFollowId(userFollow.getFollowId());
            followVO.setUserId(userFollow.getUserId());

            UserFollow one = lambdaQuery().eq(UserFollow::getUserId, userFollow.getFollowId())
                    .eq(UserFollow::getFollowId, UserContext.getUser())
                    .eq(UserFollow::getIsDelete, 0).one();
            followVO.setStatus(one == null ? 1 : 2);

            User user = userMapper.selectById(userFollow.getFollowId());
            followVO.setUsername(user.getUsername())
                    .setAvatar(user.getImg())
                    .setSignature(user.getSignature())
                    .setGender(user.getGender());
            return followVO;
        }).toList();

        return new PageDTO<>(userFollowPage.getTotal(), userFollowPage.getPages(), followVOS);
    }

    @Override
    @Transactional
    public void unfollow(Long followId) {
        Assert.notNull(followId, "用户ID不能为空");
        //删除自身关注
        lambdaUpdate().eq(UserFollow::getFollowId, followId)
                .eq(UserFollow::getUserId, UserContext.getUser())
                        .remove();
    }

    @Override
    @Transactional
    public void follow(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");
        
        Long currentUserId = UserContext.getUser();
        Assert.isTrue(!currentUserId.equals(userId), "不能关注自己");
        
        // 检查是否已经关注
        UserFollow existFollow = lambdaQuery()
                .eq(UserFollow::getUserId, currentUserId)
                .eq(UserFollow::getFollowId, userId)
                .one();
        if (existFollow != null) {
            return;
        }
        
        // 创建关注记录
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(currentUserId);
        userFollow.setFollowId(userId);
        userFollow.setIsDelete(0);
        this.save(userFollow);
        
        log.info("用户 {} 关注了用户 {}", currentUserId, userId);
    }
}
