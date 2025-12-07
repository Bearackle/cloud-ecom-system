package com.dinhhuan.loyaltyservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * Request DTO để tạo reward mới
 * 
 * Admin sử dụng API này để tạo các phần thưởng có thể đổi.
 * Metadata và image sẽ được upload lên IPFS.
 */
@Data
public class CreateRewardRequest {

    /**
     * ID của reward (unique)
     * Sẽ được lưu trên smart contract
     */
    @NotNull(message = "Reward ID is required")
    @Min(value = 1, message = "Reward ID must be positive")
    private Long rewardId;

    /**
     * Số token cần để đổi reward này
     * 
     * Ví dụ: 500 tokens để đổi voucher giảm 10%
     */
    @NotNull(message = "Cost is required")
    @Min(value = 1, message = "Cost must be at least 1")
    private Long cost;

    /**
     * Metadata của reward (sẽ upload lên IPFS)
     * 
     * Các field gợi ý:
     * - name: Tên reward
     * - description: Mô tả chi tiết
     * - terms: Điều khoản sử dụng
     * - expiry: Ngày hết hạn
     * - category: Loại reward (discount, freeship, gift...)
     */
    @NotNull(message = "Metadata is required")
    private Map<String, Object> metadata;

    /**
     * Image base64 (optional)
     * 
     * Nếu có, sẽ được upload lên IPFS
     * Format: "data:image/png;base64,..." hoặc chỉ base64 string
     */
    private String imageBase64;
}
