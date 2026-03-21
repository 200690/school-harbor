package com.harbor.reportservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.utils.UserContext;
import com.harbor.reportservice.domain.dto.ReportHandleDTO;
import com.harbor.reportservice.domain.po.ReportHandlePO;
import com.harbor.reportservice.domain.po.ReportPO;
import com.harbor.reportservice.enums.HandleActionEnum;
import com.harbor.reportservice.enums.ReportStatusEnum;
import com.harbor.reportservice.enums.ReportTypeEnum;
import com.harbor.reportservice.mapper.ReportHandle;
import com.harbor.reportservice.service.IReportHandleService;
import com.harbor.reportservice.service.IReportService;
import com.harbor.utils.client.PartTimeClient;
import com.harbor.utils.client.SecondHandClient;
import com.harbor.utils.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 举报处理服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReportHandleImpl extends ServiceImpl<ReportHandle, ReportHandlePO> implements IReportHandleService {

    private final IReportService reportService;
    private final PartTimeClient partTimeClient;
    private final SecondHandClient secondHandClient;
    private final UserClient userClient;

    /**
     * 处理举报
     *
     * @param dto 处理举报DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleReport(ReportHandleDTO dto) {
        Long currentUserId = UserContext.getUser();

        // 查询举报记录
        ReportPO report = reportService.getById(dto.getReportId());
        if (report == null) {
            throw new RuntimeException("举报记录不存在");
        }

        // 检查举报状态，已处理的不能重复处理
        if (!ReportStatusEnum.PENDING.getCode().equals(report.getStatus())) {
            throw new RuntimeException("该举报已处理，不能重复处理");
        }

        // 保存处理记录
        ReportHandlePO handlePO = new ReportHandlePO();
        handlePO.setReportId(dto.getReportId());
        handlePO.setHandlerId(currentUserId);
        handlePO.setAction(dto.getAction());
        this.save(handlePO);

        // 根据处理动作确定新的状态和处理结果
        Integer newStatus;
        String handleResult;

        if (HandleActionEnum.IGNORE.getCode().equals(dto.getAction())) {
            // 忽略操作，状态为正常处理
            newStatus = ReportStatusEnum.HANDLED_NORMAL.getCode();
            handleResult = "举报无效，已忽略";
        } else {
            // 其他操作，状态为违规处理
            newStatus = ReportStatusEnum.HANDLED_VIOLATION.getCode();
            handleResult = HandleActionEnum.getDescByCode(dto.getAction());

            // 删除对应的兼职或二手商品
            deleteReportedContent(dto.getReportType(), report.getTargetId());
        }

        // 更新举报记录状态
        reportService.lambdaUpdate()
                .eq(ReportPO::getId, dto.getReportId())
                .set(ReportPO::getStatus, newStatus)
                .set(ReportPO::getHandlerId, currentUserId)
                .set(ReportPO::getHandleTime, LocalDateTime.now())
                .set(ReportPO::getHandleResult, handleResult)
                .update();

        log.info("处理举报成功: reportId={}, action={}, handlerId={}", dto.getReportId(), dto.getAction(), currentUserId);
    }

    /**
     * 根据举报类型删除对应的内容
     *
     * @param reportType 举报类型
     * @param targetId    目标ID
     */
    private void deleteReportedContent(Integer reportType, Long targetId) {
        if (targetId == null) {
            return;
        }

        try {
            // 根据举报类型调用对应的删除接口
            if (ReportTypeEnum.PART_TIME.getCode().equals(reportType)) {
                // 删除兼职
                partTimeClient.deleteItem(targetId);
                log.info("删除兼职成功: targetId={}", targetId);
            } else if (ReportTypeEnum.ITEM.getCode().equals(reportType)) {
                // 删除二手商品
                secondHandClient.delete(targetId);
                log.info("删除二手商品成功: targetId={}", targetId);
            }
            // 评论和用户的删除逻辑暂不处理，可根据需要扩展
        } catch (Exception e) {
            log.error("删除被举报内容失败: reportType={}, targetId={}, error={}", reportType, targetId, e.getMessage(), e);
            // 不抛出异常，避免影响举报处理流程
        }
    }
}
