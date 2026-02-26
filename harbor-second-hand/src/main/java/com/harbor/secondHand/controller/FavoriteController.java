package com.harbor.secondHand.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.secondHand.domain.vo.FavoriteVO;
import com.harbor.secondHand.service.IFavorite;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api("商品收藏接口")
@RequestMapping("/second-hand/favorite")
@RequiredArgsConstructor
@Slf4j
@RestController
public class FavoriteController {
    private final IFavorite favoriteService;

    @PostMapping("MyFavorites")
    @ApiOperation("获取我的收藏列表")
    public Result<PageDTO<FavoriteVO>> getMyFavorites(@RequestBody PageQuery pageQuery) {
        log.info("获取我的收藏列表");
        PageDTO<FavoriteVO> pageDTO = favoriteService.getMyFavorites(pageQuery);
        return Result.success(pageDTO);
    }

//    @PostMapping("addFavorite")
//    @ApiOperation("添加收藏")

    @ApiOperation("移除收藏")
    @DeleteMapping("removeFavorite/{id}")
    public Result removeFavorite(@PathVariable Long id) {
        log.info("移除收藏: {}", id);
        favoriteService.removeFavorite(id);
        return Result.success();
    }
}
