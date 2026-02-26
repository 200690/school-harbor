package com.harbor.secondHand.user.controller.user;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.secondHand.user.domain.vo.FollowListVO;
import com.harbor.secondHand.user.service.IUserFollowService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
