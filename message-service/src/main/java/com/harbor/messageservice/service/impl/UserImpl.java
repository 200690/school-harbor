package com.harbor.messageservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.messageservice.domain.po.UserProfilePO;
import com.harbor.messageservice.domain.po.UserStatisticsPO;
import com.harbor.messageservice.domain.vo.UserCenterVO;
import com.harbor.messageservice.mapper.UserMapper;
import com.harbor.messageservice.service.IItemService;
import com.harbor.messageservice.service.IJobService;
import com.harbor.messageservice.service.IUserService;
import com.harbor.messageservice.service.IUserStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserImpl extends ServiceImpl<UserMapper, UserProfilePO> implements IUserService {
    private final RedisTemplate<String, Object> redisTemplate;

    private final IUserStatisticsService userStatisticsService;

    @Override
    public UserCenterVO getUserCenter(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");

//        从redis缓存中读取个人中心信息
        String cacheKey = "user:center:" + userId;
        UserCenterVO userCenterVO = (UserCenterVO) redisTemplate.opsForValue().get(cacheKey);
        if (userCenterVO != null) {
            log.info("从缓存用户个人中心数据: {}", userId);
            return userCenterVO;
        }

        UserProfilePO userProfilePO = lambdaQuery().eq(UserProfilePO::getUserId, userId).one();
        UserCenterVO centerVO = BeanUtil.copyProperties(userProfilePO, UserCenterVO.class);

//        统计数据拷贝
        UserStatisticsPO userStatisticsPO = userStatisticsService.getOne((new LambdaQueryWrapper<UserStatisticsPO>()).eq(UserStatisticsPO::getUserId, userId));
        UserCenterVO userCenterVO1 = UserCenterVO.countCpToVo(centerVO, userStatisticsPO);

//        写入缓存
        redisTemplate.opsForValue().set(cacheKey, userCenterVO1, 1, java.util.concurrent.TimeUnit.HOURS);
        log.info("缓存用户个人中心数据: {}", userId);
        return userCenterVO1;
    }
}
