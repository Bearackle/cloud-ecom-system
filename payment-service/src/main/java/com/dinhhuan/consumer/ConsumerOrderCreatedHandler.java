package com.dinhhuan.consumer;

import com.dinhhuan.dto.CreateOrderDto;
import com.dinhhuan.dto.PaymentCreation;
import com.dinhhuan.dto.enums.PaymentMethod;
import com.dinhhuan.repository.PaymentRepository;
import com.dinhhuan.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ConsumerOrderCreatedHandler {
    private final PaymentService paymentService;
    @Bean
    public Consumer<CreateOrderDto> consumerCreatedOrder() {
        return order -> {
            paymentService.createPayment(PaymentCreation.builder()
                    .orderId(order.getOrderId())
                    .method(PaymentMethod.values()[order.getMethod()])
                            .totalAmount(order.getTotalAmount())
            .build());
        };
    }
}
