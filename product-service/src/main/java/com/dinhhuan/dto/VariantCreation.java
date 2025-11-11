package com.dinhhuan.dto;

import com.dinhhuan.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantCreation {
    private Long productId;
    private String variantName;
    private Integer quantity;
    private String imgUrl;
}
