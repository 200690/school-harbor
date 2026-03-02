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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserImpl extends ServiceImpl<UserMapper, UserProfilePO> implements IUserService {
    private final IItemService itemService;
    private final IJobService jobService;
    private final IUserStatisticsService userStatisticsService;

    @Override
    public UserCenterVO getUserCenter(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");
        UserProfilePO userProfilePO = lambdaQuery().eq(UserProfilePO::getUserId, userId).one();
        UserCenterVO centerVO = BeanUtil.copyProperties(userProfilePO, UserCenterVO.class);
//        统计数据拷贝
        UserStatisticsPO userStatisticsPO = userStatisticsService.getOne((new LambdaQueryWrapper<UserStatisticsPO>()).eq(UserStatisticsPO::getUserId, userId));
        return UserCenterVO.countCpToVo(centerVO, userStatisticsPO);
    }
}
