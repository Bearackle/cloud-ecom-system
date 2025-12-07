package com.dinhhuan.loyaltyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Response DTO khi tạo reward thành công
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRewardResponse {

    /**
     * ID của reward đã tạo
     */
    private Long rewardId;

    /**
     * Các transaction hashes
     */
    private Map<String, String> transactions;

    /**
     * Thông tin IPFS
     */
    private IPFSInfo ipfs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IPFSInfo {
        private String metadataCid;
        private String imageCid;
        private String metadataUrl;
        private String imageUrl;
    }
}
