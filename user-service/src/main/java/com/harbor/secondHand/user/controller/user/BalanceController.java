package com.harbor.secondHand.user.controller.user;

import com.harbor.common.result.Result;
import com.harbor.secondHand.user.domain.dto.RechargeDTO;
import com.harbor.secondHand.user.service.IbalanceService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@ApiOperation("用户余额相关接口")
@Slf4j
@RestController
@RequestMapping("/user/balance")
@RequiredArgsConstructor
public class BalanceController {
    private final IbalanceService balanceService;
    @ApiOperation("用户充值接口")
    @PostMapping("recharge")
    public Result recharge(@RequestBody @Validated RechargeDTO rechargeDTO){
        log.info("开始充值,rechargeDTO：{}", rechargeDTO);
        balanceService.recharge(rechargeDTO);
        return Result.success();
    }

    @ApiOperation("用户消费接口")
    @PostMapping("consume/{payNo}/{userId}")
    public Result consume(@PathVariable BigDecimal payNo, @PathVariable Long userId){
        log.info("开始消费金额：{}", payNo);
        balanceService.consume(payNo , userId);
        return Result.success();
    }

    @ApiOperation("退款用户余额接口")
    @PostMapping("refund/{payNo}/{userId}")
    public Result refund(@PathVariable BigDecimal payNo, @PathVariable Long userId){
        log.info("开始退款金额：{}", payNo);
        balanceService.refund(payNo , userId);
        return Result.success();
    }

    @ApiOperation("交易完成接口")
    @PostMapping("orderComplete/{buyerId}/{sellerId}/{payNo}")
    public Result orderComplete(@PathVariable Long buyerId , @PathVariable BigDecimal payNo, @PathVariable Long sellerId){
        balanceService.orderComplete(buyerId, payNo, sellerId);
        return Result.success();
    }
}
