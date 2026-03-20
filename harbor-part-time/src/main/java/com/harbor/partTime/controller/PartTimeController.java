package com.harbor.partTime.controller;

import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.result.Result;
import com.harbor.partTime.domain.dto.PartTimeCreateDTO;
import com.harbor.partTime.domain.dto.PartTimeQueryDTO;
import com.harbor.partTime.domain.dto.adminPageDTO;
import com.harbor.partTime.domain.vo.MyJobs;
import com.harbor.partTime.domain.vo.PartTimeDetailVO;
import com.harbor.partTime.domain.vo.PartTimeVO;
import com.harbor.partTime.service.IPartTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@ApiOperation("兼职相关接口")
@Api(tags = "兼职管理")
@Slf4j
@RestController
@RequestMapping("/part-time")
@RequiredArgsConstructor
public class PartTimeController {

    private final IPartTimeService partTimeService;

    @ApiOperation("获取兼职列表接口")
    @GetMapping("/list")
    public Result<PageDTO> list(PartTimeQueryDTO partTimeQueryDTO){
        log.info("获取兼职列表");
        PageDTO<PartTimeVO> pageDTO = partTimeService.queryPartTimeList(partTimeQueryDTO);
        return Result.success(pageDTO);
    }

    @ApiOperation("发布兼职详情接口")
    @PostMapping("/newJob")
    public Result newJob(@RequestBody @Validated PartTimeCreateDTO partTimeCreateDTO){
        log.info("发布兼职: {}", partTimeCreateDTO);
        partTimeService.newJob(partTimeCreateDTO);
        return Result.success();
    }

    @ApiOperation("获取我发布的兼职详情接口")
    @PostMapping("/myJobs")
    public Result<PageDTO<MyJobs>> getMyJobs(@RequestBody PageQuery pageQuery){
        return Result.success(partTimeService.getMyJobs(pageQuery));
    }

    @ApiOperation("修改兼职状态接口")
    @PutMapping("/{id}/{status}")
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status){
        log.info("修改兼职状态: {}", id);
        partTimeService.updateStatus(id, status);
        return Result.success();
    }

    @ApiOperation("修改兼职信息接口")
    @PostMapping("/changeJob")
    public Result<Void> update(@RequestBody @Validated PartTimeCreateDTO partTimeDTO){
        log.info("修改兼职信息: {}", partTimeDTO);
        partTimeService.updateById(partTimeDTO);
        return Result.success();
    }

    @ApiOperation("删除兼职接口")
    @DeleteMapping("/deleteJob/{id}")
    public Result<Void> delete(@PathVariable Long id){
        log.info("删除兼职: {}", id);
        partTimeService.removeJobById(id);
        return Result.success();
    }

    @ApiOperation("获取兼职详情接口")
    @GetMapping("/job/{id}")
    public Result<PartTimeDetailVO> get(@PathVariable Long id){
        log.info("获取兼职详情: {}", id);
        return Result.success(partTimeService.getJobById(id));
    }

    @ApiOperation("获取所有兼职列表接口")
    @PostMapping("/getAll")
    public Result<PageDTO<PartTimeVO>> getAll(@RequestBody adminPageDTO adminPageDTO){
        return Result.success(partTimeService.getAll(adminPageDTO));
    }

    @ApiOperation("删除已发布的兼职")
    @DeleteMapping("/deleteItem/{id}")
    public Result<Void> deleteItem(@PathVariable Long id){
        log.info("删除已发布的兼职: {}", id);
        partTimeService.deleteItem(id);
        return Result.success();
    }
}
