package com.dinhhuan.order.mapper;

import com.dinhhuan.order.dto.request.ItemRequest;
import com.dinhhuan.order.dto.response.ItemResponse;
import com.dinhhuan.order.model.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toEntity(ItemRequest request);
    ItemResponse toResponse(Item entity);
}
