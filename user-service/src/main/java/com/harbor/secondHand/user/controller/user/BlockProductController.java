package com.harbor.secondHand.user.controller.user;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.secondHand.user.service.IBlockProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/blacklist/item")
@ApiOperation("用户拉黑商品接口")
public class BlockProductController {

    private final IBlockProductService blockProductService;

    @PostMapping("list")
    @ApiOperation("拉黑商品列表")
    public Result<PageDTO> list(@RequestBody PageQuery pageQuery){
        log.info("拉黑商品列表");
        //TODO 改为查Redis
        return Result.success(blockProductService.getMyBlockProducts(pageQuery));
    }

    @PostMapping("remove/{id}")
    @ApiOperation("取消拉黑商品")
    public Result remove(@PathVariable Long id){
        log.info("取消拉黑商品: {}", id);
        blockProductService.removeByItemId(id);
        return Result.success();
    }
}
