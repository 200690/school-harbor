package com.harbor.secondHand.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.result.Result;
import com.harbor.secondHand.domain.dto.ItemQueryConditionDTO;
import com.harbor.secondHand.domain.vo.ItemDetailVO;
import com.harbor.secondHand.domain.vo.ItemListItemVO;
import com.harbor.secondHand.service.ISecondHandService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@Api(tags = "二手市场接口")
@RequestMapping("/second-hand")
public class ItemController {
    private final ISecondHandService secondHandService;

    @GetMapping("/list")
    public Result<PageDTO> list(ItemQueryConditionDTO itemQueryConditionDTO) {
        log.info("获取二手商品列表接口: {}", itemQueryConditionDTO);
        PageDTO<ItemListItemVO> pageDTO  = secondHandService.querySecondHandItemList(itemQueryConditionDTO);
        return Result.success(pageDTO);
    }

    @GetMapping("/{id}")
    public Result<ItemDetailVO> getById(@PathVariable Long id) {
        log.info("获取商品详情接口: {}", id);
        ItemDetailVO itemDetailVO = secondHandService.getItemById(id);
        return Result.success(itemDetailVO);
    }
}