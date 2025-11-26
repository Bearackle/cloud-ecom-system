package com.dinhhuan.service;

import com.dinhhuan.dto.request.OrderRequest;
import com.dinhhuan.dto.response.OrderHistoryDto;
import com.dinhhuan.dto.response.OrderResponse;
import com.dinhhuan.dto.response.OrderResponseDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    String createOrder(OrderRequest orderRequest);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllUserOrder(Long userId);
    Page<OrderResponse> getAllOrders(Pageable pageable);
    OrderResponse changeStatus(Long id, Integer status);
    OrderResponseDetails getOrderByIdIncluded(Long id);
    List<OrderHistoryDto> getAllHistoryUserOrder(Long userId);
}
