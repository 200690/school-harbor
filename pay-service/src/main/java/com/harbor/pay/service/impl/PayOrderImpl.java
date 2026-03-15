package com.harbor.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.pay.domain.dto.CreateDTO;
import com.harbor.pay.domain.dto.OrderMessageDTO;
import com.harbor.pay.domain.po.PayOrderPo;
import com.harbor.pay.mapper.PayOrderMapper;
import com.harbor.pay.service.IPayOrderService;
import com.harbor.utils.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayOrderImpl extends ServiceImpl<PayOrderMapper, PayOrderPo> implements IPayOrderService {
    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPayOrderFromMessage(OrderMessageDTO orderMessageDTO) {
        log.info("开始创建支付订单，业务订单号: {}", orderMessageDTO.getOrderNo());

        // 生成支付订单号
        String payOrderNo = generatePayOrderNo();

        // 创建支付订单对象
        PayOrderPo payOrderPo = new PayOrderPo();
        payOrderPo.setPayOrderNo(payOrderNo);
        payOrderPo.setBizOrderNo(orderMessageDTO.getOrderNo());
        payOrderPo.setBizOrderId(orderMessageDTO.getOrderId());
        payOrderPo.setBuyerId(orderMessageDTO.getBuyerId());
        payOrderPo.setSellerId(orderMessageDTO.getSellerId());
        payOrderPo.setTotalAmount(orderMessageDTO.getTotalAmount());
        payOrderPo.setPayAmount(orderMessageDTO.getTotalAmount()); // 实付金额等于总金额
        payOrderPo.setPayStatus(2); // 支付成功状态（因为扣款已经在second-hand服务完成）
        payOrderPo.setPayTime(LocalDateTime.now());
        payOrderPo.setRefundStatus(0); // 无退款

        // 保存支付订单到数据库
        this.save(payOrderPo);

        log.info("支付订单创建成功，支付订单号: {}, 业务订单号: {}", payOrderNo, orderMessageDTO.getOrderNo());
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
