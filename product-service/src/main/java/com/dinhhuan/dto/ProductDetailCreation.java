package com.dinhhuan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailCreation {
    private String productName;
    private Long price;
    private Long categoryId;
    private Long brandId;
    private String description;
    private List<ProductAttributeCreation> attributes;
    private List<ProductImage> images;
}

