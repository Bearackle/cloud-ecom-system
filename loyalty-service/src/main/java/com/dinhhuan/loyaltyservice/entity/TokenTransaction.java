package com.dinhhuan.loyaltyservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok. Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho một giao dịch token trên blockchain
 * 
 * Mapping với bảng: token_transactions
 * 
 * Mỗi khi backend gọi issueTokens() hoặc frontend gọi redeemReward(),
 * một record sẽ được tạo ở đây để tracking. 
 */
@Entity
@Table(name = "token_transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Địa chỉ ví Ethereum của khách hàng
     * Format: 0x + 40 hex characters (42 total)
     */
    @Column(name = "customer_address", nullable = false, length = 42)
    private String customerAddress;
    
    /**
     * Số lượng token
     * Sử dụng BigDecimal để đảm bảo độ chính xác (18 decimals)
     */
    @Column(name = "amount", nullable = false, precision = 36, scale = 18)
    private BigDecimal amount;
    
    /**
     * Loại giao dịch:
     * - ISSUE: Phát hành token (backend -> customer)
     * - REDEEM: Đổi thưởng (customer -> contract)
     */
    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType;
    
    /**
     * ID đơn hàng (chỉ có khi type = ISSUE)
     * Dùng để đảm bảo mỗi đơn hàng chỉ được thưởng 1 lần
     */
    @Column(name = "order_id", length = 50)
    private String orderId;
    
    /**
     * ID phần thưởng (chỉ có khi type = REDEEM)
     */
    @Column(name = "reward_id")
    private Integer rewardId;
    
    /**
     * Transaction hash trên blockchain
     * Format: 0x + 64 hex characters (66 total)
     * Dùng để verify transaction trên Etherscan
     */
    @Column(name = "tx_hash", nullable = false, length = 66)
    private String txHash;
    
    /**
     * Block number khi transaction được mine
     * Dùng để xác định transaction đã được confirm
     */
    @Column(name = "block_number")
    private Long blockNumber;
    
    /**
     * Thời gian tạo record
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}