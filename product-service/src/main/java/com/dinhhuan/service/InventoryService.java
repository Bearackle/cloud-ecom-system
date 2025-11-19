package com.dinhhuan.service;

import com.dinhhuan.dto.CreateOrderDto;

public interface InventoryService {
    void proceedOrder(CreateOrderDto createOrderDto);
    void rollbackOrder(Long orderId);
}
