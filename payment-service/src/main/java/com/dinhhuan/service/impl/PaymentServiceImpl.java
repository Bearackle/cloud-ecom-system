package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.PaymentCreation;
import com.dinhhuan.dto.PaymentDto;
import com.dinhhuan.dto.enums.PaymentMethod;
import com.dinhhuan.dto.enums.PaymentStatus;
import com.dinhhuan.model.Order;
import com.dinhhuan.model.Payment;
import com.dinhhuan.repository.OrderRepository;
import com.dinhhuan.repository.PaymentRepository;
import com.dinhhuan.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final VNPayService vnPayService;
    private final MoMoService moMoService;
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
                .method(creation.getMethod())
                .createdDate(LocalDateTime.now())
                .status(PaymentStatus.PENDING)
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
                        .createdDate(p.getCreatedDate())
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
                                .createdDate(p.getCreatedDate())
                                .status(p.getStatus())
                                .id(p.getId())
                                .build())
                .toList();
    }
    @Override
    public String getPaymentUrl(Long orderId, Long amount) {
        String url;
        try {
            var payment = getPayment(orderId);
            if (payment == null) {
                log.error("Payment not found for order: {}", orderId);
                return null;
            }

            // return url based on payment method
            if (payment.getMethod() == PaymentMethod.VN_PAY) {
                url = vnPayService.createOrder(amount.intValue(), String.valueOf(orderId), "");
            } else if (payment.getMethod() == PaymentMethod.MOMO) {
                url = moMoService.createOrder(amount, String.valueOf(orderId), String.valueOf(orderId));
            } else if (payment.getMethod() == PaymentMethod.COD) {
                log.info("COD payment, no URL needed");
                return null; // COD không cần URL thanh toán
            } else {
                throw new IllegalArgumentException("Unsupported payment method: " + payment.getMethod());
            }
        } catch (Exception e) {
            log.error("Failed to create payment URL for order: {}", orderId, e);
            url = null;
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
                .createdDate(payment.getCreatedDate())
                .build();
    }
}
