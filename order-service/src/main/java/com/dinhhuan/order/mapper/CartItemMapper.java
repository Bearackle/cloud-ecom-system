package com.dinhhuan.order.mapper;

import com.dinhhuan.order.dto.request.CartItemRequest;
import com.dinhhuan.order.dto.response.CartItemResponse;
import com.dinhhuan.order.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductVariantMapper.class)
public interface CartItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    CartItem toEntity(CartItemRequest request);

    @Mapping(target = "productVariant", source = "productVariant")
    @Mapping(target = "userId", expression = "java(entity.getUser() != null ? entity.getUser().getId() : null)")
    CartItemResponse toResponse(CartItem entity);
}

