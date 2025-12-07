package com.dinhhuan.loyaltyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO khi verify certificate
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCertificateResponse {

    /**
     * Certificate có hợp lệ không
     */
    private boolean valid;

    /**
     * Đã sử dụng chưa
     */
    private boolean redeemed;

    /**
     * Mã voucher
     */
    private String voucherCode;

    /**
     * Thông tin reward
     */
    private Long rewardId;

    /**
     * Địa chỉ ví sở hữu
     */
    private String customerAddress;

    /**
     * Thời gian tạo
     */
    private LocalDateTime createdAt;

    /**
     * Thời gian sử dụng (nếu đã dùng)
     */
    private LocalDateTime redeemedAt;

    /**
     * Lý do không hợp lệ (nếu invalid)
     */
    private String invalidReason;
}
