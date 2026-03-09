package com.harbor.secondHand.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.exception.ForbiddenException;
import com.harbor.secondHand.user.config.JwtProperties;
import com.harbor.secondHand.user.domain.dto.LoginFormDTO;
import com.harbor.secondHand.user.domain.dto.UserMessageDTO;
import com.harbor.secondHand.user.domain.po.UserBalance;
import com.harbor.secondHand.user.mapper.BalanceMapper;
import com.harbor.secondHand.user.producer.UserMessageProducer;
import com.harbor.utils.dto.UserInfoDTO;
import com.harbor.secondHand.user.domain.dto.UserRegisterDTO;
import com.harbor.secondHand.user.domain.po.User;
import com.harbor.secondHand.user.domain.vo.UserLoginVO;
import com.harbor.secondHand.user.mapper.UserMapper;
import com.harbor.secondHand.user.service.IUserService;
import com.harbor.secondHand.user.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final PasswordEncoder passwordEncoder;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserMessageProducer userMessageProducer;

    private final BalanceMapper balanceMapper;

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
        // if (!passwordEncoder.matches(password, user.getPassword())) {
        // throw new BadRequestException("用户名或密码错误");
        // }
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
        // TODO 用户注册的密码加密写入数据库
        User user = new User()
                .setUsername(userRegisterDTO.getUsername())
                .setPassword(userRegisterDTO.getPassword())
                .setPhone(userRegisterDTO.getPhone())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());

        Assert.isNull(lambdaQuery().eq(User::getPhone, user.getPhone()).one(), "用户已存在");
        this.save(user);
        // 发送用户信息创建消息
        UserMessageDTO messageDTO = UserMessageDTO.userToUserMessageDTO(user);
        userMessageProducer.sendUserMessage(messageDTO, "CREATE");
        messageDTO.setBalance(0);
        log.info("用户注册成功：{}", user);
    }

    /**
     * 查询用户信息
     *
     * @return 用户信息
     */
    @Override
    public UserInfoDTO getUserInfo(Long id) {
        // 尝试从缓存获取
        String cacheKey = "user:info:" + id;
        UserInfoDTO cachedUserInfo = (UserInfoDTO) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUserInfo != null) {
            log.info("从缓存获取用户信息: {}", id);
            return cachedUserInfo;
        }

        User user = checkUserById(id);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtil.copyProperties(user, userInfoDTO);

        // 缓存结果，设置1天过期
        redisTemplate.opsForValue().set(cacheKey, userInfoDTO, 1, java.util.concurrent.TimeUnit.DAYS);
        log.info("缓存用户信息: {}", id);

        return userInfoDTO;
    }

    /**
     * 更新用户信息
     *
     * @param userInfoDTO 用户信息
     */
    @Override
    public void updateUserInfo(UserInfoDTO userInfoDTO) {
        User user = checkUserById(userInfoDTO.getId());
        BeanUtil.copyProperties(userInfoDTO, user);
        this.updateById(user);

        // 发送用户信息更新消息
        UserMessageDTO messageDTO = UserMessageDTO.userToUserMessageDTO(user);
        userMessageProducer.sendUserMessage(messageDTO, "CREATE");
        UserBalance userBalance = balanceMapper.selectOne(new QueryWrapper<UserBalance>().eq("user_id", user.getId()));
        if (userBalance != null) {
            messageDTO.setBalance(userBalance.getBalance());
        } else {
            messageDTO.setBalance(0);
        }
        userMessageProducer.sendUserMessage(messageDTO, "UPDATE");

        // 清除缓存
        String cacheKey = "user:info:" + userInfoDTO.getId();
        redisTemplate.delete(cacheKey);
        log.info("清除用户信息缓存: {}", userInfoDTO.getId());
    }

    private User checkUserById(Long id) {
        if (id == null || id < 0) {
            throw new ForbiddenException("用户不存在");
        }
        User user = lambdaQuery().eq(User::getId, id).one();
        if (BeanUtil.isEmpty(user)) {
            throw new ForbiddenException("用户不存在");
        }
        if (user.getStatus() != 1) {
            throw new ForbiddenException("用户状态异常");
        }
        return user;
    }
}
