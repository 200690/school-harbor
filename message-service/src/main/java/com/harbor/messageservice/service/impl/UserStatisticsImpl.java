package com.harbor.messageservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.messageservice.domain.po.UserStatisticsPO;
import com.harbor.messageservice.mapper.UserStatisticsMapper;
import com.harbor.messageservice.service.IUserStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserStatisticsImpl extends ServiceImpl<UserStatisticsMapper, UserStatisticsPO> implements IUserStatisticsService {
}
