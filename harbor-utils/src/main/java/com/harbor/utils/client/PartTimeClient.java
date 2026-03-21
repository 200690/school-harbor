package com.harbor.utils.client;

import com.harbor.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "part-time")
public interface PartTimeClient {

    @DeleteMapping("/part-time/deleteItem/{id}")
    Result<Void> deleteItem(@PathVariable Long id);
}
