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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockUserServiceServiceImpl extends ServiceImpl<UserBlockUserMapper, UserBlockUser> implements IBlockUserService {
    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageDTO<BlockUserVO> getMyBlockUsers(PageQuery pageQuery) {
        // 生成缓存key（存储所有拉黑的id集合）
        String cacheKey = "user:block:ids:" + pageQuery.getId();
        
        // 尝试从缓存获取所有拉黑的id集合
        List<Long> blockIds = (List<Long>) redisTemplate.opsForValue().get(cacheKey);
        
        // 缓存不存在，从数据库查询所有拉黑的id
        if (blockIds == null) {
            List<UserBlockUser> blockUsers = lambdaQuery()
                    .eq(UserBlockUser::getUserId, pageQuery.getId())
                    .eq(UserBlockUser::getStats, UserBlockUser.STATS_ACTIVE)
                    .list();
            blockIds = blockUsers.stream()
                    .map(UserBlockUser::getBlockId)
                    .collect(java.util.stream.Collectors.toList());
            
            // 写入缓存，设置1天过期
            redisTemplate.opsForValue().set(cacheKey, blockIds, 1, TimeUnit.DAYS);
            log.info("缓存拉黑用户id集合: userId={}, size={}", pageQuery.getId(), blockIds.size());
        }
        
        // 构建分页查询
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

        PageDTO<BlockUserVO> result = new PageDTO<>( blockUserPage.getTotal(), blockUserPage.getPages(), blockUserVOS);
        
        log.info("拉黑用户列表：{}", blockUserVOS);
        return result;
    }

    @Override
    public void unblock(Long blockUserId) {
        Long userId = UserContext.getUser();
        lambdaUpdate().eq(UserBlockUser::getBlockId, blockUserId)
                .eq(UserBlockUser::getUserId, userId)
                .set(UserBlockUser::getStats, UserBlockUser.STATS_INACTIVE)
                .update();
        
        // 清除缓存的拉黑用户id集合
        String cacheKey = "user:block:ids:" + userId;
        redisTemplate.delete(cacheKey);
        log.info("清除用户拉黑id集合缓存: userId={}", userId);
        // 再次缓存拉黑的用户id集合
        List<UserBlockUser> blockUsers = lambdaQuery()
                .eq(UserBlockUser::getUserId, userId)
                .eq(UserBlockUser::getStats, UserBlockUser.STATS_ACTIVE)
                .list();
        List<Long> blockIds = blockUsers.stream()
                .map(UserBlockUser::getBlockId)
                .collect(java.util.stream.Collectors.toList());

        // 写入缓存，设置1天过期
        redisTemplate.opsForValue().set(cacheKey, blockIds, 1, TimeUnit.DAYS);
        log.info("重新缓存拉黑用户id集合");
        
        // 清除兼职缓存
        String partTimeCacheKey = "job:list:recommend";
        redisTemplate.delete(partTimeCacheKey);
        log.info("清除兼职缓存: key={}", partTimeCacheKey);
        
        // 清除二手交易缓存
        String secondHandCacheKey = "item:list:latest";
        redisTemplate.delete(secondHandCacheKey);
        log.info("清除二手交易缓存: key={}", secondHandCacheKey);
    }
}
