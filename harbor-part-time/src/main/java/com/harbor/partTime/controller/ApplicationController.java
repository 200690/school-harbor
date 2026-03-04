package com.harbor.partTime.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.partTime.domain.po.ApplicationPO;
import com.harbor.partTime.domain.vo.ApplicationRecordVO;
import com.harbor.partTime.domain.vo.ApplicationerVO;
import com.harbor.partTime.service.IApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "兼职申请管理")
@RequestMapping("/part-time/apply")
public class ApplicationController {
    private final IApplicationService applicationService;

    @ApiOperation("申请兼职")
    @PostMapping( "{id}")
    public Result applyPartTime(@PathVariable Long userId) {
//        applicationService.getMyApplications(userId);
        return Result.success();
    }

    @ApiOperation("取消申请")
    @PostMapping("cancel/{partTimeId}")
    public Result cancelApply(@PathVariable Long partTimeId) {
        applicationService.cancelApply(partTimeId);
        return Result.success();
    }

    @ApiOperation("获取我的申请列表")
    @PostMapping("my-applications")
    public Result<PageDTO<ApplicationRecordVO>> getMyApplications(@RequestBody PageQuery pageQuery) {
        return Result.success(applicationService.getMyApplications(pageQuery));
    }

    @ApiOperation("获取兼职申请列表")
    @PostMapping("applyMy")
    public Result<PageDTO<ApplicationerVO>> getPartTimeApplyList(@RequestBody PageQuery pageQuery) {
        log.info("获取兼职申请列表");
        return Result.success(applicationService.getPartTimeApplyList(pageQuery));
    }

    @ApiOperation("同意兼职申请")
    @PutMapping("approve/{id}")
    public Result approveApply(@PathVariable Long id) {
        log.info("同意兼职申请: {}", id);
        applicationService.approveApply(id);
        return Result.success();
    }

    @ApiOperation("拒绝兼职申请")
    @PutMapping("reject/{id}")
    public Result rejectApply(@PathVariable Long id) {
        log.info("拒绝兼职申请: {}", id);
        applicationService.rejectApply(id);
        return Result.success();
    }
}
