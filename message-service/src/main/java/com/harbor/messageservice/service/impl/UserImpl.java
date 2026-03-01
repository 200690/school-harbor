package com.harbor.messageservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.messageservice.domain.po.UserProfilePO;
import com.harbor.messageservice.mapper.UserMapper;
import com.harbor.messageservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserImpl extends ServiceImpl<UserMapper, UserProfilePO> implements IUserService {
}
