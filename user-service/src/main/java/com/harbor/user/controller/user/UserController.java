package com.harbor.user.controller.user;

import com.harbor.user.domain.dto.LoginFormDTO;
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
    public UserLoginVO login(@RequestBody @Validated LoginFormDTO loginFormDTO){
        log.info("登录开始");
        return userService.login(loginFormDTO);
    }
}
