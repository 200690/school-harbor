package com.harbor.reportservice.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.result.Result;
import com.harbor.reportservice.domain.dto.ReportCreateDTO;
import com.harbor.reportservice.domain.dto.ReportHandleDTO;
import com.harbor.reportservice.domain.dto.ReportQueryDTO;
import com.harbor.reportservice.domain.vo.MyReportVO;
import com.harbor.reportservice.domain.vo.ReportHandleVO;
import com.harbor.reportservice.domain.vo.ReportVO;
import com.harbor.reportservice.service.IReportHandleService;
import com.harbor.reportservice.service.IReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "举报管理")
@Slf4j
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final IReportService reportService;
    private final IReportHandleService reportHandleService;

    @ApiOperation("提交举报")
    @PostMapping("/submit")
    public Result<Void> submitReport(@RequestBody @Validated ReportCreateDTO dto) {
        log.info("提交举报: reportType={}, targetId={}", dto.getReportType(), dto.getTargetId());
        reportService.createReport(dto);
        return Result.success();
    }

    @ApiOperation("处理举报")
    @PostMapping("/handle")
    public Result<Void> handleReport(@RequestBody @Validated ReportHandleDTO dto) {
        log.info("处理举报: reportId={}", dto.getReportId());
        reportHandleService.handleReport(dto);
        return Result.success();
    }

    @ApiOperation("获取举报列表（管理员）")
    @PostMapping("/list")
    public Result<PageDTO<ReportVO>> getReportList(@RequestBody ReportQueryDTO dto) {
        log.info("获取举报列表");
        return Result.success(reportService.queryReportList(dto));
    }

}
