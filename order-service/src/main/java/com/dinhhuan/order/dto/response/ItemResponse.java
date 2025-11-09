package com.dinhhuan.order.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemResponse {
    Long id;
    Long productVariantId;
    Integer quantity;
    BigDecimal price;
}
