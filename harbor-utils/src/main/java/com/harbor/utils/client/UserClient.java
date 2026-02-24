package com.harbor.utils.client;

import com.harbor.common.result.Result;
import com.harbor.utils.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user-service", path = "/user/user")
public interface UserClient {

    @GetMapping("info/{id}")
    Result<UserInfoDTO> info(@PathVariable Long id);
}
