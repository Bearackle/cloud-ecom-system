package com.dinhhuan.loyaltyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Response DTO chứa thông tin reward
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardResponse {

    /**
     * ID của reward
     */
    private Long rewardId;

    /**
     * Số token cần để đổi
     */
    private Long cost;

    /**
     * IPFS CID của metadata
     */
    private String metadataCid;

    /**
     * IPFS CID của image
     */
    private String imageCid;

    /**
     * Metadata đã parse (nếu có)
     */
    private Map<String, Object> metadata;

    /**
     * URL để truy cập image
     */
    private String imageUrl;
}
