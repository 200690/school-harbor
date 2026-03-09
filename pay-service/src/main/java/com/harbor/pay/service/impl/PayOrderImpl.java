package com.harbor.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.pay.domain.dto.CreateDTO;
import com.harbor.pay.domain.po.PayOrderPo;
import com.harbor.pay.mapper.PayOrderMapper;
import com.harbor.pay.service.IPayOrderService;
import com.harbor.utils.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayOrderImpl extends ServiceImpl<PayOrderMapper, PayOrderPo> implements IPayOrderService {
    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPayOrder(CreateDTO createDTO) {
        // 生成支付订单号
        String payOrderNo = generatePayOrderNo();
        
        // 创建支付订单对象
        PayOrderPo payOrderPo = new PayOrderPo()
                .setPayOrderNo(payOrderNo)
                .setBuyerId(createDTO.getBuyerId())
                .setSellerId(createDTO.getSellerId())
                .setTotalAmount(createDTO.getTotalAmount())
                .setPayAmount(createDTO.getPayAmount())
                .setPayMethod(3) // 假设使用余额支付
                .setPayStatus(0) // 待支付
                .setRefundStatus(0); // 无退款
        
        // 保存订单到数据库
        this.save(payOrderPo);
        log.info("创建支付订单成功，订单号: {}", payOrderNo);

        // 调用second-service微服务创建订单


        // 调用user微服务扣减余额
        userClient.consume(createDTO.getPayAmount());
        log.info("扣减用户余额成功，金额: {}", createDTO.getPayAmount());
    }
    
    /**
     * 生成支付订单号
     * 格式：PAY + 年月日时分秒 + 6位随机数
     */
    private String generatePayOrderNo() {
        StringBuilder sb = new StringBuilder();
        sb.append("PAY");
        sb.append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        sb.append(String.format("%06d", (int) (Math.random() * 1000000)));
        return sb.toString();
    }
}
