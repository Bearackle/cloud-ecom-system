package com.dinhhuan.order.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantResponse {
    Long id;
    String name;
    String imgUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
