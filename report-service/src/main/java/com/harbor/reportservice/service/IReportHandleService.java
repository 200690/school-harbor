package com.harbor.reportservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.reportservice.domain.dto.ReportHandleDTO;
import com.harbor.reportservice.domain.po.ReportHandlePO;
import com.harbor.reportservice.domain.vo.ReportHandleVO;

public interface IReportHandleService extends IService<ReportHandlePO> {

    void handleReport(ReportHandleDTO dto);
}
