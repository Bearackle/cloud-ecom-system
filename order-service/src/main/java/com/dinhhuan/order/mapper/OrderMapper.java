package com.dinhhuan.order.mapper;

import com.dinhhuan.order.dto.request.OrderRequest;
import com.dinhhuan.order.dto.response.OrderResponse;
import com.dinhhuan.order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderRequest request);

    OrderResponse toResponse(Order entity);
}
