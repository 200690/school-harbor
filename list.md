 个人中心模块交给User-Product-Job微服务模块，采用消息队列调用，重新写入缓存
4. user模块的充值金额改为BigDecimal，数据库改为decimal(10,2)
5. 充值记录调用消息队列通知
6. 