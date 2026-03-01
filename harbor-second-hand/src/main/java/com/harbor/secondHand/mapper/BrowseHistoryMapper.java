package com.harbor.secondHand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harbor.secondHand.domain.po.BrowseHistoryPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BrowseHistoryMapper extends BaseMapper<BrowseHistoryPO> {

    @Insert("INSERT INTO second_hand_browse_history(user_id, item_id, browse_time) " +
            "VALUES(#{userId}, #{itemId}, NOW()) " +
            "ON DUPLICATE KEY UPDATE browse_time = NOW()")
    int insertOrUpdate(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
