package com.dinhhuan.dto;

import com.dinhhuan.dto.enums.PaymentMethod;
import com.dinhhuan.dto.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long id;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDate createdDate;
}
