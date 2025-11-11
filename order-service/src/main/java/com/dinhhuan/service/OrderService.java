package com.dinhhuan.service;

import com.dinhhuan.dto.request.OrderRequest;
import com.dinhhuan.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    Long createOrder(OrderRequest orderRequest);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllUserOrder(Long userId);
    List<OrderResponse> getAllOrders();
    OrderResponse changeStatus(Long id, Integer status);
}
