package com.harbor.utils.client;

import com.harbor.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "part-time")
public interface PartTimeClient {
    
    @GetMapping("/part-time/getPublisherId/{id}")
    Result<Long> getPublisherId(@PathVariable Long id);
}
