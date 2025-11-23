package com.dinhhuan.service.impl;

import com.dinhhuan.util.MoMoConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoMoService {

    private final MoMoConfig moMoConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createOrder(Long amount, String orderInfo, String orderId) throws Exception {
        String requestId = UUID.randomUUID().toString();
        String requestType = "captureWallet";
        String extraData = "";

        // Build MoMo raw signature
        String rawSignature = "accessKey=" + moMoConfig.getAccessKey() +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + moMoConfig.getNotifyUrl() +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + moMoConfig.getPartnerCode() +
                "&redirectUrl=" + moMoConfig.getReturnUrl() +
                "&requestId=" + requestId +
                "&requestType=" + requestType;

        String signature = MoMoConfig.hmacSHA256(moMoConfig.getSecretKey(), rawSignature);

        // Build JSON request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", moMoConfig.getPartnerCode());
        requestBody.put("accessKey", moMoConfig.getAccessKey());
        requestBody.put("requestId", requestId);
        requestBody.put("amount", amount);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", orderInfo);
        requestBody.put("redirectUrl", moMoConfig.getReturnUrl());
        requestBody.put("ipnUrl", moMoConfig.getNotifyUrl());
        requestBody.put("requestType", requestType);
        requestBody.put("extraData", extraData);
        requestBody.put("lang", "vi");
        requestBody.put("signature", signature);

        // Call MoMo API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                MoMoConfig.MOMO_API_URL,
                HttpMethod.POST,
                entity,
                Map.class
        );

        Map<String, Object> responseMap = response.getBody();

        if (responseMap != null && Integer.parseInt(responseMap.get("resultCode").toString()) == 0) {
            return (String) responseMap.get("payUrl");
        } else {
            log.error("MoMo API error: {}", responseMap);
            throw new RuntimeException("MoMo payment creation failed: " +
                    (responseMap != null ? responseMap.get("message") : "Unknown error"));
        }
    }

    // Verify callback signature
    public boolean verifyCallback(Map<String, String> params, String signature) {
        // Build raw signature theo thứ tự MoMo callback
        String rawSignature = "accessKey=" + params.get("accessKey") +
                "&amount=" + params.get("amount") +
                "&extraData=" + params.get("extraData") +
                "&message=" + params.get("message") +
                "&orderId=" + params.get("orderId") +
                "&orderInfo=" + params.get("orderInfo") +
                "&orderType=" + params.get("orderType") +
                "&partnerCode=" + params.get("partnerCode") +
                "&payType=" + params.get("payType") +
                "&requestId=" + params.get("requestId") +
                "&responseTime=" + params.get("responseTime") +
                "&resultCode=" + params.get("resultCode") +
                "&transId=" + params.get("transId");

        String calculatedSignature = MoMoConfig.hmacSHA256(moMoConfig.getSecretKey(), rawSignature);
        return signature.equals(calculatedSignature);
    }
}