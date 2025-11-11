package com.dinhhuan.service;

import com.dinhhuan.dto.request.OrderRequest;
import com.dinhhuan.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    List<OrderResponse> getAll();
    OrderResponse getById(Long id);
    OrderResponse update(Long id, OrderRequest request);
    void delete(Long id);
}
