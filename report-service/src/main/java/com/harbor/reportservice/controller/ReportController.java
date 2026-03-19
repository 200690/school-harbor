package com.harbor.reportservice.controller;

import com.harbor.common.result.Result;
import com.harbor.reportservice.service.IReportHandleService;
import com.harbor.reportservice.service.IReportService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiOperation("举报相关接口")
@Slf4j
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final IReportService reportService;

    private final IReportHandleService reportHandleService;

    @ApiOperation("举报")
    @RequestMapping("/report")
    public Result report() {

        return Result.success();
    }

    @ApiOperation("处理举报")
    @RequestMapping("/handle")
    public Result handle() {
        return Result.success();
    }

    @ApiOperation("获取举报列表")
    @RequestMapping("/list")
    public Result list() {
        return Result.success();
    }

    @ApiOperation("获取举报处理列表")
    @RequestMapping("/handleList")
    public Result handleList() {
        return Result.success();
    }

    @ApiOperation("获取举报处理详情")
    @RequestMapping("/handleDetail")
    public Result handleDetail() {
        return Result.success();
    }

    @ApiOperation("获取我的举报列表")
    @RequestMapping("/myList")
    public Result myList() {
        return Result.success();
    }
}
