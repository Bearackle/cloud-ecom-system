package com.dinhhuan.loyaltyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO khi đăng ký khách hàng thành công
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCustomerResponse {

    /**
     * Địa chỉ ví đã đăng ký
     */
    private String walletAddress;

    /**
     * Transaction hash trên blockchain
     */
    private String transactionHash;

    /**
     * Block number khi transaction được confirm
     */
    private Long blockNumber;
}
