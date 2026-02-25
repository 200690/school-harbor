package com.harbor.partTime.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
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
}
