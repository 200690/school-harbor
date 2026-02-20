package com.harbor.secondHand.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.secondHand.domain.dto.ItemQueryConditionDTO;
import com.harbor.secondHand.domain.po.ItemPO;
import com.harbor.secondHand.domain.vo.ItemDetailVO;
import com.harbor.secondHand.domain.vo.ItemListItemVO;
import com.harbor.secondHand.mapper.SecondHandMapper;
import com.harbor.secondHand.service.IBrowseHistory;
import com.harbor.secondHand.service.ISecondHandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecondHandServiceImpl extends ServiceImpl<SecondHandMapper, ItemPO> implements ISecondHandService {

    private final IBrowseHistory browseHistory;

    @Override
    public PageDTO<ItemListItemVO> querySecondHandItemList(ItemQueryConditionDTO itemQueryConditionDTO) {
        Page<ItemPO> page = new Page<>(itemQueryConditionDTO.getPage(), itemQueryConditionDTO.getSize());
//        构建条件
        LambdaQueryWrapper<ItemPO> queryWrapper = new LambdaQueryWrapper<>();
        if(itemQueryConditionDTO.getKeyword() != null){
            queryWrapper.like(ItemPO::getTitle, itemQueryConditionDTO.getKeyword())
                    .or()
                    .like(ItemPO::getDescription, itemQueryConditionDTO.getKeyword());
        }
        queryWrapper.eq(ItemPO::getStatus, 1);
        if(itemQueryConditionDTO.getCategoryId() != null){
            queryWrapper.eq(ItemPO::getCategoryId, itemQueryConditionDTO.getCategoryId());
        }
        if(itemQueryConditionDTO.getMinPrice() != null){
            queryWrapper.ge(ItemPO::getPrice, itemQueryConditionDTO.getMinPrice());
        }
        if (itemQueryConditionDTO.getMaxPrice() != null){
            queryWrapper.le(ItemPO::getPrice, itemQueryConditionDTO.getMaxPrice());
        }
        if(itemQueryConditionDTO.getCondition() != null){
            queryWrapper.eq(ItemPO::getCondition, itemQueryConditionDTO.getCondition());
        }
        queryWrapper.eq(ItemPO::getIsDelete, 0);

        // 3. 排序处理
        if (StringUtils.hasText(itemQueryConditionDTO.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(itemQueryConditionDTO.getSortOrder());

            switch (itemQueryConditionDTO.getSortField()) {
                case "publishTime":
                    queryWrapper.orderBy(true, isAsc, ItemPO::getPublishTime);
                    break;
                case "viewCount":
                    queryWrapper.orderBy(true, isAsc, ItemPO::getViewCount);
                    break;
                case "favoriteCount":
                    queryWrapper.orderBy(true, isAsc, ItemPO::getFavoriteCount);
                    break;
                case "price":
                    queryWrapper.orderBy(true, isAsc, ItemPO::getPrice);
                    break;
                default:
                    // 默认按发布时间降序
                    queryWrapper.orderByDesc(ItemPO::getPublishTime);
            }
        } else {
            // 默认排序：按发布时间降序
            queryWrapper.orderByDesc(ItemPO::getPublishTime);
        }

        Page<ItemPO> secondHandItemPage = this.page(page, queryWrapper);
        return PageDTO.of(secondHandItemPage, ItemListItemVO.class);
    }

    @Override
    public ItemDetailVO getItemById(Long id) {
//        校验
        if(id == null){
            throw new RuntimeException("商品不存在");
        }
        ItemPO item = this.getById(id);
        if(item == null){
            throw new RuntimeException("商品不存在");
        }
//        浏览量+1，添加到浏览历史
        this.addViewCount(item);
        browseHistory.addHistory(item.getId());
//        属性拷贝
        List<ItemPO> list = lambdaQuery().eq(ItemPO::getCategoryId, item.getCategoryId()).orderByDesc(ItemPO::getPublishTime).last("LIMIT 4").list();
        ItemDetailVO itemDetailVO = new ItemDetailVO();
        itemDetailVO.setRelatedItems(itemDetailVO.convertToVO(list));
        BeanUtil.copyProperties(item, itemDetailVO);

        return itemDetailVO;
    }

    public void addViewCount(ItemPO itemPO){
        itemPO.setViewCount(itemPO.getViewCount() + 1);
        this.updateById(itemPO);
    }

}
