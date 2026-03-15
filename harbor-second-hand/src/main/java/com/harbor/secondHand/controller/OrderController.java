package com.harbor.secondHand.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.secondHand.domain.dto.CreateOrderDTO;
import com.harbor.secondHand.domain.vo.OrderListItemVO;
import com.harbor.secondHand.service.IOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = "订单接口")
@RequiredArgsConstructor
@RequestMapping("/second-hand/order")
@RestController
@Slf4j
public class OrderController {
    private final IOrder order;

    @ApiOperation("获取我的购买订单列表接口")
    @PostMapping("myOrders")
    public Result<PageDTO> getMyOrders(@RequestBody PageQuery pageQuery) {
        log.info("获取我的购买订单列表接口");
        PageDTO<OrderListItemVO> pageDTO = order.getMyOrders(pageQuery);
        return Result.success(pageDTO);
    }

    @ApiOperation(("确认收货"))
    @PostMapping("confirmReceipt/{id}")
    public Result<Void> confirmReceipt(@PathVariable Long id) {
        log.info("确认收货, id: {}", id);
        order.confirmReceipt(id);
        return Result.success();
    }

    @ApiOperation(("取消订单"))
    @PostMapping("cancelOrder/{id}")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        log.info("取消订单, id: {}", id);
        order.cancelOrder(id);
        return Result.success();
    }

    @ApiOperation("创建订单")
    @PostMapping("create")
    public Result createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        order.createOrder(createOrderDTO);
        return Result.success();
    }
}
