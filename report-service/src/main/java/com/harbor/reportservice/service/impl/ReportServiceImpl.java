package com.harbor.reportservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.reportservice.domain.po.ReportPO;
import com.harbor.reportservice.mapper.ReportMapper;
import com.harbor.reportservice.service.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportServiceImpl extends ServiceImpl<ReportMapper, ReportPO> implements IReportService {
}
