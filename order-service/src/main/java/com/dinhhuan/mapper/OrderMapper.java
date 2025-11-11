package com.dinhhuan.mapper;

import com.dinhhuan.dto.request.OrderRequest;
import com.dinhhuan.dto.response.OrderResponse;
import com.dinhhuan.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderRequest request);
    OrderResponse toResponse(Order entity);
}
