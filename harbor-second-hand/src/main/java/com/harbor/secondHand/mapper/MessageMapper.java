package com.harbor.secondHand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harbor.secondHand.domain.po.MessagePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<MessagePO> {
    int countUnreadMessages(@Param("userId") Long userId);
    void markMessagesAsRead(@Param("userId") Long userId, @Param("senderId") Long senderId);

    List<MessagePO> selectLatestMessagePerSession(@Param("userId") Long userId, @Param("page") Long page, @Param("size") Long size);

    Long countDistinctSession(@Param("userId") Long userId);

    Integer countUnreadMessagesBySession(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);
}
