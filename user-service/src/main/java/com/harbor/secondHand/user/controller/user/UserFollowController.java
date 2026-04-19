package com.harbor.secondHand.user.controller.user;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.secondHand.user.domain.vo.FollowListVO;
import com.harbor.secondHand.user.service.IUserFollowService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@ApiOperation("用户关注接口")
@RequiredArgsConstructor
@RequestMapping("/user/follow")
@Slf4j
public class UserFollowController {
    private final IUserFollowService userFollowService;

    @PostMapping("myFollow")
    @ApiOperation("获取用户关注列表")
    public Result<PageDTO> getMyFollow(@RequestBody PageQuery pageQuery){
        log.info("获取用户关注列表");
        return Result.success(userFollowService.getMyFollow(pageQuery));
    }

    @PostMapping("unfollow/{userId}")
    @ApiOperation("取消关注")
    public Result<Void> unfollow(@PathVariable Long userId){
        log.info("取消关注");
        userFollowService.unfollow(userId);
        return Result.success();
    }

    @PostMapping("add/{userId}")
    @ApiOperation("关注用户")
    public Result<Void> follow(@PathVariable Long userId){
        log.info("关注用户: {}", userId);
        userFollowService.follow(userId);
        return Result.success();
    }
}
 