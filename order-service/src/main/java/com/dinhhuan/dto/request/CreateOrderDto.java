package com.dinhhuan.dto.request;

import com.dinhhuan.dto.PaymentMethod;
import com.dinhhuan.dto.response.ItemInventoryDto;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateOrderDto {
    private Long orderId;
    private Long userId;
    private PaymentMethod method;
    private Long totalAmount;
    List<ItemInventoryDto> items;
}
