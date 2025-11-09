package com.dinhhuan.order.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long id;
    Long userId;
    Long productVariantId;
    Integer quantity;
    ProductVariantResponse productVariant;
}

