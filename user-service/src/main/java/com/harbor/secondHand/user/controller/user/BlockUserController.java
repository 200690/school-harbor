package com.harbor.secondHand.user.controller.user;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.secondHand.user.service.IBlockUserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/blacklist/user")
@ApiOperation("用户拉黑接口")
public class BlockUserController {
    private final IBlockUserService blockUserService;

    @ApiOperation("拉黑用户列表")
    @PostMapping("list")
    public Result<PageDTO> list(@RequestBody  PageQuery pageQuery){
        log.info("拉黑用户列表");
        return Result.success(blockUserService.getMyBlockUsers(pageQuery));
    }

    @ApiOperation("取消拉黑用户")
    @PostMapping("unblock/{id}")
    public Result unblock(@PathVariable Long id){
        log.info("取消拉黑用户");
        blockUserService.unblock(id);
        return Result.success();
    }
}
