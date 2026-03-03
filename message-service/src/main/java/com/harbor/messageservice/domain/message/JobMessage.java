package com.harbor.messageservice.domain.message;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobMessage {
  private Long jobId;
  private Long userId;
  private String title;
  private String description;
  private String salary;
  private String location;
  private Integer status;
  private LocalDateTime updateTime;
  private String operationType;

}