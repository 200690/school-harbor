package com.harbor.secondHand.controller;

import cn.hutool.core.lang.Assert;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.secondHand.domain.dto.ItemCreateDTO;
import com.harbor.secondHand.domain.dto.ItemQueryConditionDTO;
import com.harbor.secondHand.domain.vo.ItemDetailVO;
import com.harbor.secondHand.domain.vo.ItemListItemVO;
import com.harbor.secondHand.domain.vo.MyItem;
import com.harbor.secondHand.service.ISecondHandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
        log.info("获取二手商品列表接口");
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

    @ApiOperation("获取个人发布的商品")
    @PostMapping("/myItems")
    public Result<PageDTO<MyItem>> getMyItems(@RequestBody PageQuery pageQuery) {
        log.info("获取个人发布的商品: {}", pageQuery);
        PageDTO<MyItem> item = secondHandService.getMyItems(pageQuery);
        return Result.success(item);
    }

    @ApiOperation("修改订单状态")
    @PutMapping("/{id}/{status}")
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        log.info("修改订单状态: {}", id);
        secondHandService.updateStatus(id, status);
        return Result.success();
    }

    @ApiOperation("修改商品接口")
    @PostMapping("/changeItem")
    public Result<Void> changeItem(@RequestBody @Validated ItemCreateDTO item) {
        log.info("修改商品接口: {}", item);
        secondHandService.updateItem(item);
        return Result.success();
    }

    @ApiOperation("删除商品接口")
    @DeleteMapping("/deleteItem/{id}")
    public Result<Void> deleteItem(@PathVariable Long id) {
        log.info("删除商品接口: {}", id);
        Assert.notNull(id, "商品不存在");
        secondHandService.removeById(id);
        return Result.success();
    }

}