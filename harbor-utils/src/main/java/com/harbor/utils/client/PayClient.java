package com.harbor.utils.client;

import com.harbor.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pay-service", path = "/pay")
public interface PayClient {
    @PostMapping("/updateOrderStatus/{itemId}/{status}")
    Result updateOrderStatus(@PathVariable Long itemId, @PathVariable Integer status);
}
