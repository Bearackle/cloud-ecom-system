package com.dinhhuan.dto;

import com.dinhhuan.dto.enums.PaymentMethod;
import com.dinhhuan.dto.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreation {
    private Long orderId;
    private PaymentMethod method;
    private Long totalAmount;
    private Long userId;
}
