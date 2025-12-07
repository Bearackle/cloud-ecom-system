package com.dinhhuan.loyaltyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO chứa thông tin khách hàng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoResponse {

    /**
     * Địa chỉ ví
     */
    private String walletAddress;

    /**
     * Đã đăng ký chưa
     */
    private boolean registered;

    /**
     * Số dư token hiện tại
     */
    private BigDecimal tokenBalance;

    /**
     * Số certificate đã có
     */
    private Integer certificateCount;

    /**
     * Danh sách certificate CIDs (từ blockchain)
     */
    private List<String> certificateCids;
}
