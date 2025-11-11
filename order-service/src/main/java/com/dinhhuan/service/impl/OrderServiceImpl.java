package com.dinhhuan.service.impl;

import com.dinhhuan.dto.request.OrderRequest;
import com.dinhhuan.dto.response.OrderResponse;
import com.dinhhuan.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    @Override
    public OrderResponse createOrder(OrderRequest request) {
        return null;
    }

    @Override
    public List<OrderResponse> getAllUserOrders(Long userId) {
        return List.of();
    }

    @Override
    public List<OrderResponse> getAll() {
        return List.of();
    }

    @Override
    public OrderResponse getById(Long id) {
        return null;
    }

    @Override
    public OrderResponse update(Long id, OrderRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
