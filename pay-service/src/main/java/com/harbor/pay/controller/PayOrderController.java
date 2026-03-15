package com.harbor.pay.controller;

import com.harbor.common.result.Result;
import com.harbor.pay.domain.dto.CreateDTO;
import com.harbor.pay.service.IPayOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@ApiOperation(value = "支付订单服务")
@RequestMapping("/pay")
public class PayOrderController {

}
