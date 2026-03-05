package com.harbor.messageservice.consumer;

import com.harbor.messageservice.config.RabbitMQConfig;
import com.harbor.messageservice.domain.message.JobMessage;
import com.harbor.messageservice.domain.po.UserJobPostsPO;
import com.harbor.messageservice.mapper.JobMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobConsumer {
  //更新status表

  private final JobMapper jobMapper;

  @RabbitListener(queues = RabbitMQConfig.JOB_QUEUE_NAME)
    public void handleJobMessage(JobMessage jobMessage, Channel channel,
        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("接收到兼职信息变化消息: {}", jobMessage);

      // 处理消息
      processJobMessage(jobMessage);

      // 确认消息
      channel.basicAck(deliveryTag, false);
      log.info("兼职信息变化消息处理成功，jobId: {}", jobMessage.getJobId());
    } catch (Exception e) {
      log.error("处理兼职信息变化消息失败: {}", e.getMessage(), e);
      // 拒绝消息，重新入队
      channel.basicNack(deliveryTag, false, true);
    }
  }

  private void processJobMessage(JobMessage jobMessage) {
    switch (jobMessage.getOperationType()) {
      case "CREATE":
      case "UPDATE":
        // 查找是否已存在
        UserJobPostsPO existingJob = jobMapper.selectByJobId(jobMessage.getJobId());
        if (existingJob != null) {
          // 更新
          existingJob.setTitle(jobMessage.getTitle());
          existingJob.setDescription(jobMessage.getDescription());
          existingJob.setSalary(jobMessage.getSalary());
          existingJob.setLocation(jobMessage.getLocation());
          existingJob.setStatus(jobMessage.getStatus());
          existingJob.setUpdateTime(jobMessage.getUpdateTime());
          jobMapper.updateById(existingJob);
          log.info("更新兼职信息: jobId={}", jobMessage.getJobId());
        } else {
          // 创建
          UserJobPostsPO newJob = UserJobPostsPO.builder()
              .jobId(jobMessage.getJobId())
              .userId(jobMessage.getUserId())
              .title(jobMessage.getTitle())
              .description(jobMessage.getDescription())
              .salary(jobMessage.getSalary())
              .location(jobMessage.getLocation())
              .status(jobMessage.getStatus())
              .build();
          jobMapper.insert(newJob);
          log.info("创建兼职信息: jobId={}", jobMessage.getJobId());
        }
        break;
      case "DELETE":
        // 删除
        jobMapper.deleteById(jobMessage.getJobId());
        log.info("删除兼职信息: jobId={}", jobMessage.getJobId());
        break;
      default:
        log.warn("未知的操作类型: {}", jobMessage.getOperationType());
    }
  }
}
