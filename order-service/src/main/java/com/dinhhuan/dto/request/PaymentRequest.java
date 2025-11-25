package com.dinhhuan.dto.request;

import com.dinhhuan.dto.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Long orderId;
    private Long amount;
    private PaymentMethod paymentMethod;
}
