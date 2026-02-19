package com.harbor.partTime.controller;

import com.harbor.common.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiOperation("兼职相关接口")
@Slf4j
@RestController
@RequestMapping("/part-time")
@RequiredArgsConstructor
public class PartTimeController {

    @ApiOperation("获取兼职列表接口")
    @RequestMapping("/list")
    public Result list(){
        log.info("获取兼职列表");

        return Result.success();
    }
}
