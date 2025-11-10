package com.dinhhuan.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OrderRequest {
    Long userId;
    List<ItemRequest> items;
    Float totalAmount;
    String note;
}