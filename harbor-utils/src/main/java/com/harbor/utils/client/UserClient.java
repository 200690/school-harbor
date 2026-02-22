package com.harbor.utils.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service")
@Slf4j
public class UserClient {

}
