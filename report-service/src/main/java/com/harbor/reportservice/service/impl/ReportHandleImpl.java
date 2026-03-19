package com.harbor.reportservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.reportservice.domain.po.ReportHandlePO;
import com.harbor.reportservice.mapper.ReportHandle;
import com.harbor.reportservice.service.IReportHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportHandleImpl extends ServiceImpl<ReportHandle, ReportHandlePO> implements IReportHandleService {
}
