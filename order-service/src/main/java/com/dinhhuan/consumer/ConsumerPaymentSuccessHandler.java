package com.dinhhuan.consumer;

import com.dinhhuan.enums.OrderStatus;
import com.dinhhuan.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ConsumerPaymentSuccessHandler {
    private final OrderService orderService;
    @Bean
    public Consumer<Long> consumerSuccessPayment(){
        return orderId ->{
            orderService.changeStatus(orderId, OrderStatus.PAID.ordinal());
        };
    }
}
