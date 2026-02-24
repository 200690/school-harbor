package com.harbor.partTime.controller;

import com.harbor.partTime.service.IFavoriteService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "收藏申请管理")
@RequestMapping("/part-time/favorite")
public class FavoriteController {
    private final IFavoriteService favoriteService;
}
