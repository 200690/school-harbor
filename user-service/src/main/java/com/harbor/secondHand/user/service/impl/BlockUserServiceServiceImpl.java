package com.harbor.secondHand.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.utils.UserContext;
import com.harbor.secondHand.user.domain.po.User;
import com.harbor.secondHand.user.domain.po.UserBlockUser;
import com.harbor.secondHand.user.domain.vo.BlockUserVO;
import com.harbor.secondHand.user.mapper.UserBlockUserMapper;
import com.harbor.secondHand.user.mapper.UserMapper;
import com.harbor.secondHand.user.service.IBlockUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockUserServiceServiceImpl extends ServiceImpl<UserBlockUserMapper, UserBlockUser> implements IBlockUserService {
    private final UserMapper userMapper;

    @Override
    public PageDTO<BlockUserVO> getMyBlockUsers(PageQuery pageQuery) {
        Page<UserBlockUser> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        LambdaQueryWrapper<UserBlockUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserBlockUser::getUserId, pageQuery.getId())
                .eq(UserBlockUser::getStats, UserBlockUser.STATS_ACTIVE)
                .orderByDesc(UserBlockUser::getBlockTime);

        Page<UserBlockUser> blockUserPage = this.page(page, wrapper);

        if(blockUserPage.getTotal() == 0)
            return null;

        List<BlockUserVO> blockUserVOS = blockUserPage.getRecords().stream().map(blockUser -> {
            BlockUserVO blockUserVO = new BlockUserVO();
            BeanUtil.copyProperties(blockUser, blockUserVO);
            User user = userMapper.selectById(blockUser.getBlockId());
            blockUserVO.setUsername(user.getUsername())
                    .setAvatar(user.getImg())
                    .setSignature(user.getSignature());
            return blockUserVO;
        }).toList();

        log.info("拉黑用户列表：{}", blockUserVOS);
        return new PageDTO<>( blockUserPage.getTotal(), blockUserPage.getPages(), blockUserVOS);
    }

    @Override
    public void unblock(Long blockUserId) {
        lambdaUpdate().eq(UserBlockUser::getBlockId, blockUserId)
                .eq(UserBlockUser::getUserId, UserContext.getUser())
                .set(UserBlockUser::getStats, UserBlockUser.STATS_INACTIVE)
                .update();
    }
}
