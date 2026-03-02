package com.harbor.messageservice.controller;

import com.harbor.common.result.Result;
import com.harbor.messageservice.domain.vo.UserCenterVO;
import com.harbor.messageservice.service.IUserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZLL15
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags = "用户冗余")
@RequestMapping("/message/user")
public class UserController {
    private final IUserService userService;

    @GetMapping("/center/{id}")
    public Result<UserCenterVO> getUserProfile(@PathVariable Long id) {
        log.info("获取用户信息");
        return Result.success(userService.getUserCenter(id));
    }
}
