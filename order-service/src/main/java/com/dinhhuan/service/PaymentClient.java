package com.dinhhuan.service;

import com.dinhhuan.dto.PaymentUrlResponse;
import com.dinhhuan.dto.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "payment-service")
public interface PaymentClient {
    @PostMapping("/payments/generate-url")
    PaymentUrlResponse generatePaymentUrl(@RequestBody PaymentRequest request);
}