package com.harbor.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.exception.ForbiddenException;
import com.harbor.user.config.JwtProperties;
import com.harbor.user.domain.dto.LoginFormDTO;
import com.harbor.user.domain.po.User;
import com.harbor.user.domain.vo.UserLoginVO;
import com.harbor.user.mapper.UserMapper;
import com.harbor.user.service.IUserService;
import com.harbor.user.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final PasswordEncoder passwordEncoder;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    @Override
    public UserLoginVO login(LoginFormDTO loginDTO) {
        // 1.数据校验
        String phone = loginDTO.getPhone();
        String password = loginDTO.getPassword();
        // 2.根据用户名或手机号查询
        User user = lambdaQuery().eq(User::getPhone, phone).one();
        Assert.notNull(user, "用户名错误");
        // 3.校验是否禁用
        if (user.getStatus() != 1) {
            throw new ForbiddenException("用户被冻结");
        }
        // 4.校验密码 TODO
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new BadRequestException("用户名或密码错误");
//        }
        Assert.isTrue(Objects.equals(password, user.getPassword()), "用户名或密码错误");
        log.info("user1:{}", user);
        // 5.生成TOKEN
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());
        // 6.封装VO返回
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setBalance(user.getBalance());
        vo.setToken(token);
        log.info("user2:{}", vo);
        return vo;
    }
}
