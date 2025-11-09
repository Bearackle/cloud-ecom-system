package com.dinhhuan.order.dto.request;

import com.dinhhuan.order.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    Long userId;
    List<ItemRequest> items;
    BigDecimal totalAmount;
    String note;
    OrderStatus status;
}