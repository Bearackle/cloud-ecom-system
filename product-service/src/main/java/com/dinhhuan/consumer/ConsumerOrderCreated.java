package com.dinhhuan.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ConsumerOrderCreated {
    private final product paymentService;
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
