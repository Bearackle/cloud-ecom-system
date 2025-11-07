package com.dinhhuan.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductDetailEdit {
    private String productName;
    private Long price;
    private Long categoryId;
    private Long brandId;
    private String description;
    private List<AttributeDto> attributes;
    private List<ImageDto> images;
}
