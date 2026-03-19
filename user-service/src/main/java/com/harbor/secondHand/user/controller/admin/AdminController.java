package com.harbor.secondHand.user.controller.admin;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.secondHand.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@ApiOperation("管理员相关接口")
@Slf4j
@RestController
@RequestMapping("/user/admin")
@RequiredArgsConstructor
public class AdminController {
    private final IUserService userService;

    @ApiOperation("获取用户列表")
    @PostMapping("/list")
    public Result<PageDTO> getUserList(@RequestBody PageQuery pageQuery) {
        log.info("获取用户列表");
        return Result.success(userService.getUserList(pageQuery));
    }

    @ApiOperation("封禁用户")
    @PostMapping("/ban/{id}")
    public Result<Void> banUser(@PathVariable Long id) {
        log.info("封禁用户");
        userService.banUser(id);
        return Result.success();
    }

    @ApiOperation("解封用户")
    @PostMapping("/unban/{id}")
    public Result<Void> unbanUser(@PathVariable Long id) {
        log.info("解封用户");
        userService.unbanUser(id);
        return Result.success();
    }
}
