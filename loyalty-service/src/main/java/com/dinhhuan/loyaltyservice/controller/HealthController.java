package com.dinhhuan.loyaltyservice.controller;

import com.dinhhuan.loyaltyservice.dto.response.ApiResponse;
import com.dinhhuan.loyaltyservice.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * 
 * Cung cấp endpoint để kiểm tra service đang chạy và các kết nối
 */
@Slf4j
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final BlockchainService blockchainService;

    /**
     * Health check endpoint
     * 
     * GET /health
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "loyalty-service");

        // Check blockchain connection
        try {
            boolean connected = blockchainService.isConnected();
            health.put("blockchain", connected ? "CONNECTED" : "DISCONNECTED");

            if (connected) {
                Long blockNumber = blockchainService.getCurrentBlockNumber();
                health.put("currentBlock", blockNumber);
                health.put("tokenContract", blockchainService.getLoyaltyTokenAddress());
                health.put("managerContract", blockchainService.getLoyaltyManagerAddress());
            }
        } catch (Exception e) {
            health.put("blockchain", "ERROR: " + e.getMessage());
        }

        return ResponseEntity.ok(ApiResponse.success(health));
    }

    /**
     * Simple ping endpoint
     * 
     * GET /health/ping
     */
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "pong");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
