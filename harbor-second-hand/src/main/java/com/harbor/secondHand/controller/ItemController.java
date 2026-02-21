package com.harbor.secondHand.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.result.Result;
import com.harbor.secondHand.domain.dto.ItemCreateDTO;
import com.harbor.secondHand.domain.dto.ItemQueryConditionDTO;
import com.harbor.secondHand.domain.po.ItemPO;
import com.harbor.secondHand.domain.vo.ItemDetailVO;
import com.harbor.secondHand.domain.vo.ItemListItemVO;
import com.harbor.secondHand.service.ISecondHandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@Api(tags = "二手市场接口")
@RequestMapping("/second-hand")
public class ItemController {
    private final ISecondHandService secondHandService;

    /**
     * 获取二手商品列表接口
     * @param itemQueryConditionDTO
     * @return
     */
    @GetMapping("/list")
    public Result<PageDTO> list(ItemQueryConditionDTO itemQueryConditionDTO) {
        log.info("获取二手商品列表接口: {}", itemQueryConditionDTO);
        PageDTO<ItemListItemVO> pageDTO  = secondHandService.querySecondHandItemList(itemQueryConditionDTO);
        return Result.success(pageDTO);
    }

    /**
     * 获取商品详情接口
     * @param id
     * @return
     */
    @GetMapping("/item/{id}")
    public Result<ItemDetailVO> getById(@PathVariable Long id) {
        log.info("获取商品详情接口: {}", id);
        ItemDetailVO itemDetailVO = secondHandService.getItemById(id);
        return Result.success(itemDetailVO);
    }

    /**
     * 创建二手商品接口
     * @param itemCreateDTO
     * @return
     */
    @PostMapping("/products")
    public Result<Void> create(@RequestBody ItemCreateDTO itemCreateDTO) {
        log.info("创建二手商品接口: {}", itemCreateDTO);
        secondHandService.createItem(itemCreateDTO);
        return Result.success();
    }
}