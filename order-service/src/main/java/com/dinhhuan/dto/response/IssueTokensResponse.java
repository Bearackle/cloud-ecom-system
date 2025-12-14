package com.dinhhuan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO khi phát hành token thành công
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueTokensResponse {

    /**
     * Địa chỉ ví nhận token
     */
    private String walletAddress;

    /**
     * Số token đã phát hành
     */
    private Long tokensIssued;

    /**
     * Số dư token mới của khách hàng
     */
    private BigDecimal newBalance;

    /**
     * Transaction hash trên blockchain
     */
    private String transactionHash;

    /**
     * ID đơn hàng liên quan
     */
    private String orderId;
}
