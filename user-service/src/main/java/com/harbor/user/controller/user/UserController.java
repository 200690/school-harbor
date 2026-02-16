package com.harbor.user.controller.user;

import com.harbor.result.Result;
import com.harbor.user.domain.dto.LoginFormDTO;
import com.harbor.user.domain.dto.RechargeDTO;
import com.harbor.user.domain.dto.UserRegisterDTO;
import com.harbor.user.domain.vo.UserLoginVO;
import com.harbor.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@ApiOperation("用户相关接口")
@Slf4j
@RestController
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


}
