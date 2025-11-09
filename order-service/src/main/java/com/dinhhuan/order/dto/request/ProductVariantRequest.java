package com.dinhhuan.order.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantRequest {
    String name;
    String imgUrl;
}
