package com.dinhhuan.loyaltyservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok. NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity đại diện cho một Certificate/Voucher
 * 
 * Khi khách hàng đổi thưởng (redeemReward) thành công:
 * 1. Frontend gọi smart contract để burn token
 * 2. Frontend thông báo cho backend
 * 3.  Backend tạo certificate, upload lên IPFS
 * 4.  Backend gọi issueCertificate() trên blockchain
 * 5.  Lưu thông tin vào bảng này
 */
@Entity
@Table(name = "certificates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Địa chỉ ví khách hàng
     */
    @Column(name = "customer_address", nullable = false, length = 42)
    private String customerAddress;
    
    /**
     * ID phần thưởng đã đổi
     */
    @Column(name = "reward_id", nullable = false)
    private Long rewardId;
    
    /**
     * Mã voucher unique
     * Format: VOUCHER-XXXXXXXX (X = random alphanumeric)
     * Khách dùng mã này để redeem offline/online
     */
    @Column(name = "voucher_code", nullable = false, unique = true, length = 20)
    private String voucherCode;
    
    /**
     * IPFS CID của certificate metadata
     * Metadata chứa: tên reward, thông tin khách, ngày hết hạn... 
     */
    @Column(name = "ipfs_cid", nullable = false, length = 100)
    private String ipfsCid;
    
    /**
     * Hash để verify certificate
     * Tính bằng: SHA256(customerAddress + voucherCode + timestamp)
     */
    @Column(name = "verification_hash", nullable = false, length = 64)
    private String verificationHash;
    
    /**
     * Transaction hash khi gọi issueCertificate() trên blockchain
     */
    @Column(name = "tx_hash", nullable = false, length = 66)
    private String txHash;
    
    /**
     * Voucher đã được sử dụng chưa
     */
    @Column(name = "redeemed", nullable = false)
    private boolean redeemed;
    
    /**
     * Thời gian sử dụng voucher
     */
    @Column(name = "redeemed_at")
    private LocalDateTime redeemedAt;
    
    /**
     * Thời gian tạo
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime. now();
        redeemed = false;
    }
}