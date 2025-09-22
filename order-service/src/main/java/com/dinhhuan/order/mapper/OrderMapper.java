package com.dinhhuan.order.mapper;

import com.dinhhuan.order.dto.request.OrderRequest;
import com.dinhhuan.order.dto.response.OrderResponse;
import com.dinhhuan.order.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderRequest request);
    OrderResponse toResponse(Order entity);
}
