package com.dinhhuan.loyaltyservice;

import org.springframework.boot. SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main Application Class cho Loyalty Service
 * 
 * Service này đóng vai trò trung gian giữa Frontend và Blockchain,
 * xử lý các nghiệp vụ liên quan đến Loyalty Token và IPFS. 
 * 
 * Các chức năng chính:
 * 1. Quản lý đăng ký khách hàng trên blockchain
 * 2. Phát hành và quản lý Loyalty Token
 * 3. Upload/Download metadata lên IPFS (Pinata)
 * 4. Phát hành certificate sau khi đổi thưởng
 */
@SpringBootApplication
@EnableConfigurationProperties
public class LoyaltyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoyaltyServiceApplication.class, args);
    }
}