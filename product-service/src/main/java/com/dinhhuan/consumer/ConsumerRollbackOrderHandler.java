package com.dinhhuan.consumer;

import com.dinhhuan.dto.CreateOrderDto;
import com.dinhhuan.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ConsumerRollbackOrderHandler {
    private final InventoryService service;
    @Bean
    public Consumer<Long> consumerRollbackOrder() {
        return service::rollbackOrder;
    }
}
