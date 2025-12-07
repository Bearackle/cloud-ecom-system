package com.dinhhuan.loyaltyservice.dto. request;

import jakarta.validation.constraints. NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Request DTO để đăng ký khách hàng
 */
@Data
public class RegisterCustomerRequest {

    /**
     * Địa chỉ ví Ethereum của khách hàng
     * Format: 0x + 40 hex characters
     */
    @NotBlank(message = "Wallet address is required")
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Invalid Ethereum address format")
    private String walletAddress;
}