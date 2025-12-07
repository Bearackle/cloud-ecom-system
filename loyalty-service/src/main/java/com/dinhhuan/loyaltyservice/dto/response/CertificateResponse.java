package com.dinhhuan.loyaltyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO chứa thông tin certificate
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponse {

    /**
     * Mã voucher unique
     */
    private String voucherCode;

    /**
     * IPFS CID của certificate
     */
    private String ipfsCid;

    /**
     * URL để download certificate PDF
     */
    private String ipfsUrl;

    /**
     * Hash để verify
     */
    private String verificationHash;

    /**
     * Transaction hash khi issue certificate
     */
    private String transactionHash;

    /**
     * ID reward đã đổi
     */
    private Long rewardId;

    /**
     * Tên reward
     */
    private String rewardName;

    /**
     * Trạng thái đã sử dụng chưa
     */
    private boolean redeemed;

    /**
     * Thời gian tạo
     */
    private LocalDateTime createdAt;
}
