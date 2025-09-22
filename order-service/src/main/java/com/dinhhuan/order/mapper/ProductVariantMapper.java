package com.dinhhuan.order.mapper;

import com.dinhhuan.order.dto.response.ProductVariantResponse;
import com.dinhhuan.order.model.ProductVariant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ProductVariantMapper {
    ProductVariantResponse toResponse(ProductVariant entity);
}
