package com.harbor.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.exception.ForbiddenException;
import com.harbor.user.config.JwtProperties;
import com.harbor.user.domain.dto.LoginFormDTO;
import com.harbor.user.domain.dto.RechargeDTO;
import com.harbor.user.domain.dto.UserRegisterDTO;
import com.harbor.user.domain.po.RechargeRecord;
import com.harbor.user.domain.po.User;
import com.harbor.user.domain.vo.UserLoginVO;
import com.harbor.user.mapper.UserMapper;
import com.harbor.user.service.IUserService;
import com.harbor.user.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final PasswordEncoder passwordEncoder;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    /**
     * 用户登录
     *
     * @param loginDTO 登录参数
     * @return 登录结果
     */
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
        vo.setToken(token);
        log.info("user2:{}", vo);
        return vo;
    }

    /**
     * 用户注册
     *
     * @param userRegisterDTO 注册参数
     */
    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        //TODO 用户注册的密码加密写入数据库
        User user = new User()
                .setUsername(userRegisterDTO.getUsername())
                .setPassword(userRegisterDTO.getPassword())
                .setPhone(userRegisterDTO.getPhone())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());

        Assert.isNull(lambdaQuery().eq(User::getPhone, user.getPhone()).one(), "用户已存在");
        this.save(user);
        log.info("用户注册成功：{}", user);
    }
}
