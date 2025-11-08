package com.dinhhuan.dto;

import com.dinhhuan.model.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariantCreation {
    private Long productId;
    private String variantName;
    private Integer quantity;
    private String imgUrl;
}
