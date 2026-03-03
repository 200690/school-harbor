package com.harbor.messageservice.consumer;

import com.harbor.messageservice.config.RabbitMQConfig;
import com.harbor.messageservice.domain.message.ItemMessage;
import com.harbor.messageservice.domain.po.UserItemPostsPO;
import com.harbor.messageservice.mapper.ItemMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemConsumer {

    private final ItemMapper itemMapper;

    @RabbitListener(queues = RabbitMQConfig.ITEM_QUEUE_NAME)
    public void handleItemMessage(Map<String, Object> message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("接收到二手交易信息变化消息: {}", message);

            // 转换消息
            ItemMessage itemMessage = new ItemMessage();
            itemMessage.setItemId((Long) message.get("itemId"));
            itemMessage.setUserId((Long) message.get("userId"));
            itemMessage.setTitle((String) message.get("title"));
            itemMessage.setDescription((String) message.get("description"));
            itemMessage.setPrice((java.math.BigDecimal) message.get("price"));
            itemMessage.setStatus((Integer) message.get("status"));
            itemMessage.setUpdateTime((java.time.LocalDateTime) message.get("updateTime"));
            itemMessage.setOperationType((String) message.get("operationType"));

            // 处理消息
            processItemMessage(itemMessage);

            // 确认消息
            channel.basicAck(deliveryTag, false);
            log.info("二手交易信息变化消息处理成功，itemId: {}", itemMessage.getItemId());
        } catch (Exception e) {
            log.error("处理二手交易信息变化消息失败: {}", e.getMessage(), e);
            // 拒绝消息，重新入队
            channel.basicNack(deliveryTag, false, true);
        }
    }

    private void processItemMessage(ItemMessage itemMessage) {
        switch (itemMessage.getOperationType()) {
            case "CREATE":
            case "UPDATE":
                // 查找是否已存在
                UserItemPostsPO existingItem = itemMapper.selectByItemId(itemMessage.getItemId());
                if (existingItem != null) {
                    // 更新
                    existingItem.setTitle(itemMessage.getTitle());
                    existingItem.setDescription(itemMessage.getDescription());
                    existingItem.setPrice(itemMessage.getPrice());
                    existingItem.setStatus(itemMessage.getStatus());
                    existingItem.setUpdateTime(itemMessage.getUpdateTime());
                    itemMapper.updateById(existingItem);
                    log.info("更新二手交易信息: itemId={}", itemMessage.getItemId());
                } else {
                    // 创建
                    UserItemPostsPO newItem = UserItemPostsPO.builder()
                            .itemId(itemMessage.getItemId())
                            .userId(itemMessage.getUserId())
                            .title(itemMessage.getTitle())
                            .description(itemMessage.getDescription())
                            .price(itemMessage.getPrice())
                            .status(itemMessage.getStatus())
                            .build();
                    itemMapper.insert(newItem);
                    log.info("创建二手交易信息: itemId={}", itemMessage.getItemId());
                }
                break;
            case "DELETE":
                // 删除
                itemMapper.deleteByItemId(itemMessage.getItemId());
                log.info("删除二手交易信息: itemId={}", itemMessage.getItemId());
                break;
            default:
                log.warn("未知的操作类型: {}", itemMessage.getOperationType());
        }
    }
}
