package com.dinhhuan.loyaltyservice.config;

import lombok.Data;
import org.springframework.boot.context.properties. ConfigurationProperties;
import org.springframework. context.annotation.Configuration;

/**
 * Configuration cho Pinata IPFS service
 * 
 * Pinata là một dịch vụ IPFS pinning, giúp:
 * - Upload file lên IPFS dễ dàng qua REST API
 * - File được "pin" nên không bị garbage collected
 * - Có gateway để truy cập file qua HTTP
 * 
 * Đăng ký tại: https://pinata.cloud
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "pinata")
public class PinataConfig {
    
    /**
     * API Key từ Pinata dashboard
     */
    private String apiKey;
    
    /**
     * Secret Key từ Pinata dashboard
     */
    private String secretKey;
    
    /**
     * URL gateway để truy cập file
     * Format: https://gateway.pinata.cloud/ipfs/{CID}
     */
    private String gatewayUrl;
    
    /**
     * URL API của Pinata
     */
    private String apiUrl;
}