package com.harbor.partTime.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.partTime.domain.po.ApplicationPO;
import com.harbor.partTime.domain.po.PartTimePO;
import com.harbor.partTime.domain.vo.ApplicationRecordVO;
import com.harbor.partTime.mapper.ApplicationMapper;
import com.harbor.partTime.mapper.PartTimeMapper;
import com.harbor.partTime.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, ApplicationPO> implements IApplicationService {

    private final PartTimeMapper partTimeMapper;


    /**
     * 获取用户申请记录集合
     * @param userId
     * @return
     */
    @Override
    public List<ApplicationRecordVO> getMyApplications(Long userId) {
        log.info("获取用户申请记录: {}", userId);
        Assert.notNull(userId, "用户ID不能为空");

//        获取申请记录
        List<ApplicationRecordVO> recordVOList = lambdaQuery().eq(ApplicationPO::getUserId, userId).list().stream().map(item ->
        BeanUtil.copyProperties(item, ApplicationRecordVO.class)
        ).toList();
        if(recordVOList.isEmpty()){
            return recordVOList;
        }
//        获取兼职信息
        List<Long> jobsId = recordVOList.stream().map(ApplicationRecordVO::getPartTimeId).distinct().toList();
        List<PartTimePO> partTimePOS = partTimeMapper.selectList(new LambdaQueryWrapper<PartTimePO>()
                .in(PartTimePO::getId, jobsId));
        Map<Long, PartTimePO> partTimePOMap = partTimePOS.stream().collect(Collectors.toMap(PartTimePO::getId, Function.identity()));
//        封装兼职信息
        recordVOList.forEach(item -> {
            PartTimePO partTimePO = partTimePOMap.get(item.getPartTimeId());
            item.setPartTimeTitle(partTimePO.getTitle());
            item.setPartTimeLocation(partTimePO.getLocation());
            item.setEmployer(partTimePO.getEmployer());
            item.setSalaryDesc(partTimePO.getSalaryDesc());
        });
        return recordVOList;
    }
}
