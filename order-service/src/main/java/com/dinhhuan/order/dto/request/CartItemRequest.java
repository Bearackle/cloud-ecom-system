package com.dinhhuan.order.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {
    Long userId;
    Long productVariantId;
    Integer quantity;
}

