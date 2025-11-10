package com.dinhhuan.mapper;

import com.dinhhuan.dto.response.ProductVariantResponse;
import com.dinhhuan.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariantMapper INSTANCE = Mappers.getMapper(ProductVariantMapper.class);

    ProductVariantResponse toDto(ProductVariant productVariant);
}

