package com.harbor.messageservice.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserInfoVO extends UserProfileVO {
    // 商家发布的二手商品列表
    private List<ItemInfoVO> items;
    
    // 商家发布的兼职信息列表
    private List<JobInfoVO> jobs;
}
