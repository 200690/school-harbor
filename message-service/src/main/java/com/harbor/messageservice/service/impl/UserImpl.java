package com.harbor.messageservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.messageservice.domain.po.MessageProcessedPO;
import com.harbor.messageservice.domain.po.UserProfilePO;
import com.harbor.messageservice.domain.po.UserStatisticsPO;
import com.harbor.messageservice.domain.po.UserItemPostsPO;
import com.harbor.messageservice.domain.po.UserJobPostsPO;
import com.harbor.messageservice.domain.vo.UserCenterVO;
import com.harbor.messageservice.domain.vo.UserInfoVO;
import com.harbor.messageservice.domain.vo.ItemInfoVO;
import com.harbor.messageservice.domain.vo.JobInfoVO;
import com.harbor.messageservice.mapper.ItemMapper;
import com.harbor.messageservice.mapper.JobMapper;
import com.harbor.messageservice.mapper.MsgProcessedMapper;
import com.harbor.messageservice.mapper.UserMapper;
import com.harbor.messageservice.mapper.UserStatisticsMapper;
import com.harbor.messageservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserImpl extends ServiceImpl<UserMapper, UserProfilePO> implements IUserService {
    private final RedisTemplate<String, Object> redisTemplate;

    private final UserStatisticsMapper userStatisticsMapper;

    private final ItemMapper itemMapper;

    private final JobMapper jobMapper;

    private final MsgProcessedMapper msgProcessedMapper;

    @Override
    public UserCenterVO getUserCenter(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");

//        从redis缓存中读取个人中心信息
        String cacheKey = "user:center:" + userId;
        Object cacheObj = redisTemplate.opsForValue().get(cacheKey);
        if (cacheObj instanceof UserCenterVO) {
            log.info("从缓存用户个人中心数据: {}", userId);
            return (UserCenterVO) cacheObj;
        }

        UserProfilePO userProfilePO = lambdaQuery().eq(UserProfilePO::getUserId, userId).one();
        UserCenterVO centerVO = BeanUtil.copyProperties(userProfilePO, UserCenterVO.class);

//        统计数据拷贝
        UserStatisticsPO userStatisticsPO = userStatisticsMapper.selectOne
                ((new LambdaQueryWrapper<UserStatisticsPO>())
                .eq(UserStatisticsPO::getUserId, userId));
        UserCenterVO userCenterVO1 = UserCenterVO.countCpToVo(centerVO, userStatisticsPO);

        // 查询最新的3条消息通知
        List<MessageProcessedPO> recentMessages = msgProcessedMapper.selectList(
                new LambdaQueryWrapper<MessageProcessedPO>()
                        .eq(MessageProcessedPO::getUserId, userId)
                        .orderByDesc(MessageProcessedPO::getProcessedTime)
                        .last("LIMIT 3"));
        List<String> tags = recentMessages.stream()
                .map(MessageProcessedPO::getMessage)
                .toList();
        userCenterVO1.setTags(tags);

//        写入缓存
        redisTemplate.opsForValue().set(cacheKey, userCenterVO1, 1, java.util.concurrent.TimeUnit.HOURS);
        log.info("缓存用户个人中心数据: {}", userId);
        return userCenterVO1;
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");

        // 从数据库查询用户基本信息
        UserProfilePO userProfilePO = lambdaQuery().eq(UserProfilePO::getUserId, userId).one();
        Assert.notNull(userProfilePO, "用户不存在");

        // 构建 UserInfoVO 对象
        UserInfoVO userInfoVO = BeanUtil.copyProperties(userProfilePO, UserInfoVO.class);

        // 查询用户发布的二手商品列表
        List<UserItemPostsPO> itemPosts = itemMapper.selectList((new LambdaQueryWrapper<UserItemPostsPO>())
                .eq(UserItemPostsPO::getUserId, userId));
        List<ItemInfoVO> itemInfos = itemPosts.stream().map(item -> {
            ItemInfoVO itemInfo = new ItemInfoVO();
            itemInfo.setId(item.getItemId())
                    .setTitle(item.getTitle())
                    .setPrice(item.getPrice())
                    .setStatus(item.getStatus())
                    .setPublishTime(item.getCreateTime());
            return itemInfo;
        }).toList();
        userInfoVO.setItems(itemInfos);

        // 查询用户发布的兼职信息列表
        List<UserJobPostsPO> jobPosts = jobMapper.selectList((new LambdaQueryWrapper<UserJobPostsPO>())
                .eq(UserJobPostsPO::getUserId, userId));
        List<JobInfoVO> jobInfos = jobPosts.stream().map(job -> {
            JobInfoVO jobInfo = new JobInfoVO();
            jobInfo.setId(job.getJobId())
                    .setTitle(job.getTitle())
                    .setEmployer(job.getDescription()) // 这里假设 description 字段存储了招聘方信息
                    .setLocation(job.getLocation())
                    .setSalaryDesc(job.getSalary())
                    .setStatus(job.getStatus())
                    .setPublishTime(job.getCreateTime());
            return jobInfo;
        }).toList();
        userInfoVO.setJobs(jobInfos);

        log.info("获取商家信息成功: userId={}, items={}, jobs={}", userId, itemInfos.size(), jobInfos.size());
        return userInfoVO;
    }
}
