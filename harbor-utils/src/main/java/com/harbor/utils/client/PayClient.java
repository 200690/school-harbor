package com.harbor.utils.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "pay-service")
public class PayClient {
}
