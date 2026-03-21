package com.harbor.secondHand.user.controller.user;

import com.harbor.common.result.Result;
import com.harbor.secondHand.user.domain.dto.LoginFormDTO;
import com.harbor.utils.dto.UserInfoDTO;
import com.harbor.secondHand.user.domain.dto.UserRegisterDTO;
import com.harbor.secondHand.user.domain.vo.UserVO;
import com.harbor.secondHand.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@ApiOperation("用户相关接口")
@Slf4j
@RestController
@RequestMapping("/user/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @ApiOperation("用户登录接口")
    @PostMapping("login")
    public Result login(@RequestBody @Validated LoginFormDTO loginFormDTO){
        log.info("登录开始");
        return Result.success(userService.login(loginFormDTO));
    }

    @ApiOperation("用户注册接口")
    @PostMapping("register")
    public Result register(@RequestBody @Validated UserRegisterDTO userRegisterDTO){
        log.info("开始注册");
        userService.register(userRegisterDTO);
        return Result.success();
    }

    @ApiOperation("查询用户信息接口")
    @GetMapping("info/{id}")
    public Result<UserInfoDTO> info(@PathVariable Long id){
        log.info("开始查询用户信息");
        return Result.success(userService.getUserInfo(id));
    }

    @ApiOperation("更新用户信息接口")
    @PostMapping("update")
    public Result updateUserInfo(@RequestBody @Validated UserInfoDTO userInfoDTO){
        log.info("开始更新用户信息");
        userService.updateUserInfo(userInfoDTO);
        return Result.success();
    }

    @ApiOperation("扣减信誉分")
    @PostMapping("deductCredit/{id}")
    public Result deductCredit(@PathVariable Long id){
        log.info("开始扣减信誉分");
        userService.deductCredit(id);
        return Result.success();
    }
}
