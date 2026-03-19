package com.harbor.utils.client;

import com.harbor.common.result.Result;
import com.harbor.utils.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@FeignClient(name = "user-service", path = "/user")
public interface UserClient {

    @GetMapping("user/info/{id}")
    Result<UserInfoDTO> info(@PathVariable Long id);

    @PostMapping("/balance/consume/{payNo}/{userId}")
    Result consume(@PathVariable BigDecimal payNo, @PathVariable Long userId);

    @PostMapping("balance/refund/{payNo}/{userId}")
    Result refund(@PathVariable BigDecimal payNo, @PathVariable Long userId);

    @PostMapping("balance/orderComplete/{buyerId}/{sellerId}/{payNo}")
    Result orderComplete(@PathVariable Long buyerId , @PathVariable BigDecimal payNo, @PathVariable Long sellerId);
}
