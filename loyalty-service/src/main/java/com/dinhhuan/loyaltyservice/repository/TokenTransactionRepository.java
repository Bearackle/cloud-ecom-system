package com.dinhhuan.loyaltyservice.repository;

import org.springframework.data. jpa.repository. JpaRepository;
import org.springframework. stereotype.Repository;

import com.dinhhuan.loyaltyservice.entity.TokenTransaction;

import java.util.List;

/**
 * Repository cho TokenTransaction
 */
@Repository
public interface TokenTransactionRepository extends JpaRepository<TokenTransaction, Long> {
    
    /**
     * Tìm tất cả giao dịch của một khách hàng
     */
    List<TokenTransaction> findByCustomerAddress(String customerAddress);
    
    /**
     * Tìm tất cả giao dịch theo loại
     */
    List<TokenTransaction> findByTransactionType(String transactionType);
    
    /**
     * Kiểm tra order đã được thưởng token chưa
     * (Tránh thưởng trùng lặp cho cùng một đơn hàng)
     */
    boolean existsByOrderId(String orderId);
    
    /**
     * Tìm giao dịch theo transaction hash
     */
    TokenTransaction findByTxHash(String txHash);
}