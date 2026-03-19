package com.harbor.messageservice.domain.vo;

import com.harbor.messageservice.domain.po.UserStatisticsPO;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ZLL15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserCenterVO extends UserProfileVO{
    private Integer publicCount;
    private Integer applicationCount;
    private Integer buyCounnt;
    private Integer favCount;

    private String role;

    private List<String> tags;

    public static UserCenterVO countCpToVo(UserCenterVO vo, UserStatisticsPO po){
        return vo.setApplicationCount(po.getItemApplyCount() + po.getJobApplyCount())
                .setPublicCount(po.getItemPostCount() + po.getJobPostCount())
                .setBuyCounnt(po.getItemPurchaseCount())
                .setFavCount(po.getItemFavoriteCount() + po.getJobFavoriteCount());
    }
}
