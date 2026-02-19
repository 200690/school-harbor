package com.harbor.partTime.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.result.Result;
import com.harbor.partTime.domain.dto.PartTimeQueryDTO;
import com.harbor.partTime.domain.vo.PartTimeVO;
import com.harbor.partTime.service.IPartTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiOperation("兼职相关接口")
@Api(tags = "兼职管理")
@Slf4j
@RestController
@RequestMapping("/part-time")
@RequiredArgsConstructor
public class PartTimeController {

    private final IPartTimeService partTimeService;
    @ApiOperation("获取兼职列表接口")
    @RequestMapping("/list")
    public Result list(PartTimeQueryDTO partTimeQueryDTO){
        log.info("获取兼职列表");
        PageDTO<PartTimeVO> pageDTO = partTimeService.queryPartTimeList(partTimeQueryDTO);
        return Result.success(pageDTO);
    }
}
