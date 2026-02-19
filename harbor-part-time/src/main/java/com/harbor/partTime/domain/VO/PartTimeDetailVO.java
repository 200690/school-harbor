package com.harbor.partTime.domain.VO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 兼职详情VO（继承基础VO，可添加额外信息）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PartTimeDetailVO extends PartTimeVO {

    // 发布者信息
    private PublisherInfoVO publisherInfo;

    // 相关推荐
    private List<PartTimeVO> recommendList;

    @Data
    public static class PublisherInfoVO {
        private Long id;
        private String nickname;
        private String avatar;
        private Integer creditScore;
        private Integer publishCount;
    }
}