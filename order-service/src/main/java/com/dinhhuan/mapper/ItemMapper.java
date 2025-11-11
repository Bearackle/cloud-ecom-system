package com.dinhhuan.mapper;

import com.dinhhuan.dto.request.ItemRequest;
import com.dinhhuan.dto.response.ItemResponse;
import com.dinhhuan.model.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toEntity(ItemRequest request);
    ItemResponse toResponse(Item entity);
}
