package com.dinhhuan.dto;

import com.dinhhuan.dto.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDto {
    private Long orderId;
    private PaymentMethod method;
    private Long totalAmount;
    private Long userId;
}
