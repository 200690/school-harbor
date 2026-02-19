package com.harbor.secondHand.user.controller.user;

import com.harbor.common.result.Result;
import com.harbor.secondHand.user.domain.dto.RechargeDTO;
import com.harbor.secondHand.user.service.IbalanceService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
