package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.PaymentCreation;
import com.dinhhuan.dto.PaymentDto;
import com.dinhhuan.model.Order;
import com.dinhhuan.model.Payment;
import com.dinhhuan.repository.OrderRepository;
import com.dinhhuan.repository.PaymentRepository;
import com.dinhhuan.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final VNPayService vnPayService;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final DefaultUidGenerator uid;
    @Override
    public Long createPayment(PaymentCreation creation) {
        var order = Order.builder()
                .id(creation.getOrderId())
                .totalAmount(creation.getTotalAmount())
                .userId(creation.getUserId())
                .build();
        orderRepository.save(order);
        Payment payment = Payment.builder()
                        .id(uid.getUID())
                        .order(order)
                .build();
        paymentRepository.save(payment);
        return payment.getId();
    }
    @Override
    public Long updateStatus(Long id, PaymentDto payment) {
         var entity = paymentRepository.findById(id).orElse(null);
         if (entity == null) {
             return null;
         }
         entity.setStatus(payment.getStatus());
         paymentRepository.save(entity);
         return entity.getId();
    }
    @Override
    public List<PaymentDto> getAllUserPayments(Long userId) {
        var ls = paymentRepository.findAllByUserId(userId);
        return ls.stream().map(
                p -> PaymentDto.builder()
                        .method(p.getMethod())
                        .createdDate(p.getCreatedDate().toLocalDate())
                        .status(p.getStatus())
                        .id(p.getId())
                        .build())
                .collect(Collectors.toList());
    }
    @Override
    public List<PaymentDto> getAllPayments() {
        var payls = paymentRepository.findAll();
        return payls.stream().map(
                        p -> PaymentDto.builder()
                                .method(p.getMethod())
                                .createdDate(p.getCreatedDate().toLocalDate())
                                .status(p.getStatus())
                                .id(p.getId())
                                .build())
                .collect(Collectors.toList());
    }
    @Override
    public String getPaymentUrl(Long orderId, Long amount) {
        String url;
        try {
            url = vnPayService.createOrder(amount.intValue(), "nothing","");
        } catch (Exception e) {
            url =  null;
        }
        return url;
    }

    @Override
    public PaymentDto getPayment(Long orderId) {
        var ls = paymentRepository.findByOrderIdOrderByCreatedDateDesc(orderId);
        if(ls == null || ls.isEmpty()) {
            return null;
        }
        Payment payment = ls.getFirst();
        return PaymentDto.builder().id(payment.getId())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .createdDate(payment.getCreatedDate().toLocalDate())
                .build();
    }
}
