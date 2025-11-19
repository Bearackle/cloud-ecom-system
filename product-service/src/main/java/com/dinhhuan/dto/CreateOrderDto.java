package com.dinhhuan.dto;

import com.dinhhuan.dto.ItemInventoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateOrderDto {
    private Long orderId;
    private Long totalAmount;
    List<ItemInventoryDto> items;
}
