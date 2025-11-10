package com.dinhhuan.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OrderResponse {
    Long id;
    Long userId;
    LocalDateTime orderDate;
    Float totalAmount;
    String note;
    Integer status;
    List<ItemResponse> items;
}
