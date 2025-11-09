package com.dinhhuan.order.mapper;

import com.dinhhuan.order.dto.request.ProductVariantRequest;
import com.dinhhuan.order.dto.response.ProductVariantResponse;
import com.dinhhuan.order.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductVariant toEntity(ProductVariantRequest request);

    ProductVariantResponse toResponse(ProductVariant entity);
}
