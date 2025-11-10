package com.dinhhuan.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ItemResponse {
    Long id;
    Long quantity;
    Float price;
}
