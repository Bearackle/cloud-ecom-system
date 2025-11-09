package com.dinhhuan.order.dto.response;

import com.dinhhuan.order.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    Long userId;
    BigDecimal totalAmount;
    String note;
    OrderStatus status;
    LocalDateTime orderDate;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<ItemResponse> items;
}
