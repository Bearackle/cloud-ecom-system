package com.dinhhuan.dto.mapper;


import com.dinhhuan.dto.ProductDto;
import com.dinhhuan.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    ProductDto toDto(Product product);
}

