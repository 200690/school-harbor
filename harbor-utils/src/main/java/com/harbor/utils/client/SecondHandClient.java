package com.harbor.utils.client;

import com.harbor.common.result.Result;
import com.harbor.utils.dto.ItemMainDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "second-hand")
public interface SecondHandClient {

    @GetMapping("/second-hand/getItemInfo/{id}")
    Result<ItemMainDTO> getItemInfo(@PathVariable Long id);

    @DeleteMapping("/second-hand/delete/{id}")
    Result<Void> delete(@PathVariable Long id);
}
