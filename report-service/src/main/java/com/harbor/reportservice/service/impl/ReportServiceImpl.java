package com.harbor.reportservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.utils.UserContext;
import com.harbor.reportservice.domain.dto.ReportCreateDTO;
import com.harbor.reportservice.domain.dto.ReportQueryDTO;
import com.harbor.reportservice.domain.po.ReportPO;
import com.harbor.reportservice.domain.vo.ReportVO;
import com.harbor.reportservice.enums.ReportReasonEnum;
import com.harbor.reportservice.enums.ReportStatusEnum;
import com.harbor.reportservice.enums.ReportTypeEnum;
import com.harbor.reportservice.mapper.ReportMapper;
import com.harbor.reportservice.service.IReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 举报服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl extends ServiceImpl<ReportMapper, ReportPO> implements IReportService {

    /**
     * 创建举报
     *
     * @param dto 举报创建DTO
     */
    @Override
    public void createReport(ReportCreateDTO dto) {
        Long currentUserId = UserContext.getUser();

        ReportPO report = new ReportPO();
        report.setReportType(dto.getReportType());
        report.setTargetId(dto.getTargetId());
        report.setReporterId(currentUserId);
        report.setReasonType(dto.getReasonType());
        report.setReasonDesc(dto.getReasonDesc());
        report.setStatus(ReportStatusEnum.PENDING.getCode());

        // 将图片URL列表用逗号分隔存储
        if (dto.getEvidenceImages() != null && !dto.getEvidenceImages().isEmpty()) {
            report.setEvidenceImages(String.join(",", dto.getEvidenceImages()));
        }

        this.save(report);
        log.info("创建举报成功: userId={}, reportType={}, targetId={}", currentUserId, dto.getReportType(), dto.getTargetId());
    }

    /**
     * 查询举报列表（管理员使用）
     *
     * @param dto 查询条件DTO
     * @return 分页举报列表
     */
    @Override
    public PageDTO<ReportVO> queryReportList(ReportQueryDTO dto) {
        Page<ReportPO> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        LambdaQueryWrapper<ReportPO> queryWrapper = new LambdaQueryWrapper<>();

        // 根据举报类型筛选
        if (dto.getReportType() != null) {
            queryWrapper.eq(ReportPO::getReportType, dto.getReportType());
        }
        // 根据状态筛选
        if (dto.getStatus() != null) {
            queryWrapper.eq(ReportPO::getStatus, dto.getStatus());
        }
        // 根据理由类型筛选
        if (dto.getReasonType() != null) {
            queryWrapper.eq(ReportPO::getReasonType, dto.getReasonType());
        }
        // 根据举报内容模糊查询
        if (StringUtils.hasText(dto.getReasonDesc())) {
            queryWrapper.like(ReportPO::getReasonDesc, dto.getReasonDesc());
        }

        queryWrapper.orderByDesc(ReportPO::getCreatedAt);

        Page<ReportPO> reportPage = this.page(page, queryWrapper);

        if (reportPage.getTotal() == 0) {
            return PageDTO.empty(reportPage);
        }

        List<ReportVO> voList = reportPage.getRecords().stream()
                .map(this::convertToVO)
                .toList();

        return PageDTO.of(reportPage, voList);
    }


    /**
     * 将PO转换为ReportVO
     *
     * @param po 举报PO
     * @return 举报VO
     */
    private ReportVO convertToVO(ReportPO po) {
        ReportVO vo = BeanUtil.copyProperties(po, ReportVO.class);
        vo.setReportTypeName(ReportTypeEnum.getDescByCode(po.getReportType()));
        vo.setReasonTypeName(ReportReasonEnum.getDescByCode(po.getReasonType()));
        vo.setStatusName(ReportStatusEnum.getDescByCode(po.getStatus()));

        // 将逗号分隔的图片URL字符串转换为列表
        if (StringUtils.hasText(po.getEvidenceImages())) {
            List<String> images = Arrays.asList(po.getEvidenceImages().split(","));
            vo.setEvidenceImages(images);
        } else {
            vo.setEvidenceImages(Collections.emptyList());
        }

        return vo;
    }

}
