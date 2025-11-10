package com.dinhhuan.mapper;

import com.dinhhuan.dto.response.ProductVariantResponse;
import com.dinhhuan.model.ProductVariant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ProductVariantMapper {
    ProductVariantResponse toResponse(ProductVariant entity);
}
