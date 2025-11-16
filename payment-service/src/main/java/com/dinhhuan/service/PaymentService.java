package com.dinhhuan.service;

import com.dinhhuan.dto.PaymentCreation;
import com.dinhhuan.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    Long createPayment(PaymentCreation creation);
    Long updateStatus(Long id, PaymentDto payment);
    List<PaymentDto> getAllUserPayments(Long userId);
    List<PaymentDto> getAllPayments();
}
