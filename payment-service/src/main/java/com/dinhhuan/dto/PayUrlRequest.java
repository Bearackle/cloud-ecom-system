package com.dinhhuan.dto;

import com.dinhhuan.dto.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayUrlRequest {
    private Long orderId;
    private Long amount;
    private PaymentMethod paymentMethod;
}
