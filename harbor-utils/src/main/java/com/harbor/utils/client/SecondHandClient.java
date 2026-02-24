package com.harbor.utils.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "second-hand")
public interface SecondHandClient {

}
