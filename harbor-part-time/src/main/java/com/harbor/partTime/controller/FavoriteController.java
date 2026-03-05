package com.harbor.partTime.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.common.utils.UserContext;
import com.harbor.partTime.domain.vo.FavoriteVO;
import com.harbor.partTime.service.IFavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "收藏申请管理")
@RequestMapping("/part-time/favorite")
public class FavoriteController {
    private final IFavoriteService favoriteService;

    @PostMapping("MyFavorites")
    @ApiOperation("获取我的收藏列表")
    public Result<PageDTO> getMyFavorites(@RequestBody PageQuery pageQuery) {
        log.info("获取我的收藏列表");
        PageDTO<FavoriteVO> pageDTO = favoriteService.getMyFavorites(pageQuery);
        return Result.success(pageDTO);
    }

    @DeleteMapping("removeFavorite/{id}")
    @ApiOperation("移除收藏")
    public Result<Void> removeFavorite(@PathVariable Long id) {
        log.info("移除收藏: {}", id);
        favoriteService.removeFavorite(id);
        return Result.success();
    }

    @ApiOperation("添加收藏")
    @PostMapping("addFavorite/{partTimeId}")
    public Result addFavorite(@PathVariable Long partTimeId) {
        log.info("添加收藏: {}", partTimeId);
        favoriteService.addFavorite(partTimeId);
        return Result.success();
    }
}
