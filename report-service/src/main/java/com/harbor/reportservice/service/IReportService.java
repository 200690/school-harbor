package com.harbor.reportservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.reportservice.domain.dto.ReportCreateDTO;
import com.harbor.reportservice.domain.dto.ReportQueryDTO;
import com.harbor.reportservice.domain.po.ReportPO;
import com.harbor.reportservice.domain.vo.MyReportVO;
import com.harbor.reportservice.domain.vo.ReportVO;

public interface IReportService extends IService<ReportPO> {

    void createReport(ReportCreateDTO dto);

    PageDTO<ReportVO> queryReportList(ReportQueryDTO dto);
}
