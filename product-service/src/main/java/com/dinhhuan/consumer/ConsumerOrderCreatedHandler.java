package com.dinhhuan.consumer;

import com.dinhhuan.dto.CreateOrderDto;
import com.dinhhuan.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ConsumerOrderCreated {
    private final InventoryService service;
    @Bean
    public Consumer<CreateOrderDto> consumerCreatedOrder() {
        return service::proceedOrder;
    }
}
