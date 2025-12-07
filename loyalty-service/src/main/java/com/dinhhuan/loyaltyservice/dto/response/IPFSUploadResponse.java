package com.dinhhuan.loyaltyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO khi upload file lên IPFS thành công
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IPFSUploadResponse {

    /**
     * IPFS Content Identifier
     * Dùng để truy cập file: gateway/ipfs/{cid}
     */
    private String ipfsCid;

    /**
     * Full URL để truy cập file qua Pinata gateway
     */
    private String ipfsUrl;

    /**
     * Kích thước file (bytes)
     */
    private Long fileSize;

    /**
     * MIME type của file
     */
    private String mimeType;
}
