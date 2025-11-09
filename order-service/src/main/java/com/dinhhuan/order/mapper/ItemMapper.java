package com.dinhhuan.order.mapper;

import com.dinhhuan.order.dto.request.ItemRequest;
import com.dinhhuan.order.dto.response.ItemResponse;
import com.dinhhuan.order.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    Item toEntity(ItemRequest request);

    ItemResponse toResponse(Item entity);
}
