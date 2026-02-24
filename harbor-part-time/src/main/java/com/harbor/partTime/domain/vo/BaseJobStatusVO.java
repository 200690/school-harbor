package com.harbor.partTime.domain.vo;

import lombok.Data;

@Data
public class BaseJobStatusVO {
    /**
     * 是否被当前用户收藏
     * @return true-被收藏，false-否
     */
    private Boolean isFavorite;

    /**
     * 当前用户是否可以申请
     * @return true-可以申请，false-否
     */
    private Boolean applicable;

    /**
     * 当前用户是否是发布者
     * @return true-是发布者，false-否
     */
    private Boolean isPublisher;
}
