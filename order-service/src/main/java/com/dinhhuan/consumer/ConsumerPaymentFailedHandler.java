package com.dinhhuan.consumer;

import com.dinhhuan.dto.request.AddressSyncDto;
import com.dinhhuan.enums.OrderStatus;
import com.dinhhuan.producer.RollbackOrderEvent;
import com.dinhhuan.service.AddressService;
import com.dinhhuan.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ConsumerPaymentFailedHandler {
    private final RollbackOrderEvent event;
    private final OrderService orderService;
    @Bean
    public Consumer<Long> consumerFailedPayment(){
        return orderId ->{
            orderService.changeStatus(orderId, OrderStatus.CANCELED.ordinal());
            event.sendMessage(orderId);
        };
    }
}
