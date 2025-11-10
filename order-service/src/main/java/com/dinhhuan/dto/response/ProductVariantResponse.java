package com.dinhhuan.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ProductVariantResponse {
    Long id;
    Long productId;
    String name;
}
