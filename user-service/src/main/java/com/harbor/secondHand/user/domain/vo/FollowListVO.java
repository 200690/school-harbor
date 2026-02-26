package com.harbor.secondHand.user.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 关注列表视图对象
 */
@Data
@Accessors(chain = true)
public class FollowListVO {
    /**
     * 关注总数
     */
    private Long total;

    /**
     * 粉丝总数
     */
    private Long fansTotal;

    /**
     * 互相关注数
     */
    private Long mutualTotal;

    /**
     * 关注列表
     */
    private List<FollowVO> followList;

    /**
     * 粉丝列表
     */
    private List<FollowVO> fansList;
}
