package com.harbor.secondHand.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.common.utils.UserContext;
import com.harbor.secondHand.domain.dto.ItemCreateDTO;
import com.harbor.secondHand.domain.dto.ItemQueryConditionDTO;
import com.harbor.secondHand.domain.po.ItemPO;
import com.harbor.secondHand.domain.vo.ItemDetailVO;
import com.harbor.secondHand.domain.vo.ItemListItemVO;
import com.harbor.secondHand.domain.vo.MyItem;
import com.harbor.secondHand.mapper.BrowseHistoryMapper;
import com.harbor.secondHand.mapper.SecondHandMapper;
import com.harbor.secondHand.service.ISecondHandService;
import com.harbor.utils.client.UserClient;
import com.harbor.utils.dto.ItemMainDTO;
import com.harbor.utils.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecondHandServiceImpl extends ServiceImpl<SecondHandMapper, ItemPO> implements ISecondHandService {
    private final BrowseHistoryMapper browerHistory;

    private final UserClient userClient;

    @Override
    public PageDTO<ItemListItemVO> querySecondHandItemList(ItemQueryConditionDTO itemQueryConditionDTO) {
        Page<ItemPO> page = new Page<>(itemQueryConditionDTO.getPage(), itemQueryConditionDTO.getSize());
//        构建条件
        LambdaQueryWrapper<ItemPO> queryWrapper = new LambdaQueryWrapper<>();
        if (itemQueryConditionDTO.getKeyword() != null) {
            queryWrapper.and(w -> w.like(ItemPO::getTitle, itemQueryConditionDTO.getKeyword())
                    .or()
                    .like(ItemPO::getDescription, itemQueryConditionDTO.getKeyword()));
        }
        queryWrapper.eq(ItemPO::getStatus, 1);
        if(itemQueryConditionDTO.getCategoryId() != null){
            queryWrapper.eq(ItemPO::getCategoryId, itemQueryConditionDTO.getCategoryId());
            log.info("categoryId: {}", itemQueryConditionDTO.getCategoryId());
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
        browerHistory.insertOrUpdate(UserContext.getUser(), id);

//        属性拷贝
        List<ItemPO> list = lambdaQuery().eq(ItemPO::getCategoryId, item.getCategoryId()).orderByDesc(ItemPO::getPublishTime).last("LIMIT 4").list();
        ItemDetailVO itemDetailVO = new ItemDetailVO();

        List<ItemListItemVO> itemVOS = list.stream().map(po -> {
            ItemListItemVO itemListItemVO = new ItemListItemVO();
            BeanUtil.copyProperties(po, itemListItemVO);
            itemListItemVO.setConditionDesc(
                    switch (po.getCondition()) {
                        case 1 -> "全新";
                        case 2 -> "9成新";
                        case 3 -> "8成新";
                        case 4 -> "7成新及以下";
                        default -> "未知";
                    }
            );
            return itemListItemVO;
        }).toList();
        itemDetailVO.setRelatedItems(itemVOS);
        BeanUtil.copyProperties(item, itemDetailVO);
        itemDetailVO.setImages(Arrays.stream(item.getImages().split(",")).toList());
//        卖家属性拷贝
        UserInfoDTO userInfoDTO = userClient.info(itemDetailVO.getSellerId()).getData();
        itemDetailVO.setSellerName(userInfoDTO.getUsername());
        itemDetailVO.setSellerAvatar(userInfoDTO.getImg());
        return itemDetailVO;
    }

    @Override
    public void createItem(ItemCreateDTO itemCreateDTO) {
        ItemPO itemPO = new ItemPO();
        BeanUtil.copyProperties(itemCreateDTO, itemPO);
        itemPO.setSellerId(UserContext.getUser());
        this.save(itemPO);
    }

    @Override
    public PageDTO<MyItem> getMyItems(PageQuery pageQuery) {
        // 创建分页对象
        Page<ItemPO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        // 执行分页查询
        Page<ItemPO> itemPOPage = lambdaQuery()
                .eq(ItemPO::getSellerId, pageQuery.getId())
                .page(page);

        // 转换数据
        List<MyItem> myItemList = itemPOPage.getRecords().stream()
                .map(item -> BeanUtil.copyProperties(item, MyItem.class))
                .collect(Collectors.toList());

        // 封装为PageDTO
        return new PageDTO<>(itemPOPage.getPages(), itemPOPage.getTotal(), myItemList);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        ItemPO itemPO = lambdaQuery().eq(ItemPO::getId, id).one();
        Assert.notNull(itemPO, "商品不存在");
        if(Objects.equals(itemPO.getStatus(), status)){
            return;
        }
        if(itemPO.getStatus() > 1){
            throw new RuntimeException("商品状态错误");
        }
        itemPO.setStatus(status);
        this.updateById(itemPO);
    }

    @Override
    public void updateItem(ItemCreateDTO item) {
        ItemPO itemPO = lambdaQuery().eq(ItemPO::getId, item.getId()).one();
        Assert.notNull(itemPO, "商品不存在");
        BeanUtil.copyProperties(item, itemPO, CopyOptions.create().ignoreNullValue());
        itemPO.setUpdateTime(LocalDateTime.now());
        this.updateById(itemPO);
    }

    @Override
    public ItemMainDTO getItemMain(Long id) {
        Assert.notNull(id, "id为空");
        ItemPO itemPO = lambdaQuery().eq(ItemPO::getId, id).one();
        Assert.notNull(itemPO, "商品不存在");
        return BeanUtil.copyProperties(itemPO, ItemMainDTO.class);
    }

    public void addViewCount(ItemPO itemPO){
        itemPO.setViewCount(itemPO.getViewCount() + 1);
        this.updateById(itemPO);
    }

}
