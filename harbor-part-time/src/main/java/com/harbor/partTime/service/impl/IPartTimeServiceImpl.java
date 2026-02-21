package com.harbor.partTime.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.utils.UserContext;
import com.harbor.partTime.domain.dto.PartTimeCreateDTO;
import com.harbor.partTime.domain.dto.PartTimeQueryDTO;
import com.harbor.partTime.domain.po.PartTimePO;
import com.harbor.partTime.domain.vo.PartTimeVO;
import com.harbor.partTime.mapper.PartTimeMapper;
import com.harbor.partTime.service.IPartTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class IPartTimeServiceImpl extends ServiceImpl<PartTimeMapper, PartTimePO> implements IPartTimeService {

    public PageDTO<PartTimeVO> queryPartTimeList(PartTimeQueryDTO dto) {
        // 1. 构建分页参数
        Page<PartTimePO> page = new Page<>(dto.getPage(), dto.getSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<PartTimePO> wrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（标题或描述）
        if (StringUtils.hasText(dto.getKeyword())) {
            wrapper.and(w -> w
                    .like(PartTimePO::getTitle, dto.getKeyword())
                    .or()
                    .like(PartTimePO::getDescription, dto.getKeyword())
            );
        }

        wrapper.eq(PartTimePO::getStatus, 1);

        // 兼职类型筛选（支持多选）
        if (dto.getTypes() != null && !dto.getTypes().isEmpty()) {
            wrapper.in(PartTimePO::getType, dto.getTypes());
        }

        // 信誉分范围筛选
        if (dto.getCreditScore() != null) {
            // 根据信誉分范围进行筛选
            int creditScore = dto.getCreditScore();
            if (creditScore >= 81) {
                wrapper.ge(PartTimePO::getCreditScore, 81);  // 优秀
            } else if (creditScore >= 61) {
                wrapper.between(PartTimePO::getCreditScore, 61, 80);  // 中等
            } else {
                wrapper.le(PartTimePO::getCreditScore, 60);  // 差
            }
        }

        // 3. 排序处理
        if (StringUtils.hasText(dto.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(dto.getSortOrder());

            switch (dto.getSortField()) {
                case "publishTime":
                    wrapper.orderBy(true, isAsc, PartTimePO::getPublishTime);
                    break;
                case "viewCount":
                    wrapper.orderBy(true, isAsc, PartTimePO::getViewCount);
                    break;
                case "applicantCount":
                    wrapper.orderBy(true, isAsc, PartTimePO::getApplicantCount);
                    break;
                case "creditScore":
                    wrapper.orderBy(true, isAsc, PartTimePO::getCreditScore);
                    break;
                default:
                    // 默认按发布时间降序
                    wrapper.orderByDesc(PartTimePO::getPublishTime);
            }
        } else {
            // 默认排序：按发布时间降序
            wrapper.orderByDesc(PartTimePO::getPublishTime);
        }

        // 4. 执行分页查询
        Page<PartTimePO> partTimePage = this.page(page, wrapper);

        // 5. 转换为VO并返回
        return PageDTO.of(partTimePage, PartTimeVO.class);
    }
    /**
     * 发布兼职
     * @param partTimeCreateDTO
     */
    @Override
    public void newJob(PartTimeCreateDTO partTimeCreateDTO) {
        PartTimePO partTimePO = new PartTimePO();
        partTimePO.setPublisherId(UserContext.getUser());
        BeanUtil.copyProperties(partTimeCreateDTO, partTimePO);
        this.save(partTimePO);
    }
}
