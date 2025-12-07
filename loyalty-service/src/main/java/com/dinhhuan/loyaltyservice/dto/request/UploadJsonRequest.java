package com.dinhhuan.loyaltyservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * Request DTO để upload JSON lên IPFS
 */
@Data
public class UploadJsonRequest {

    /**
     * Dữ liệu JSON cần upload
     */
    @NotNull(message = "Data is required")
    private Map<String, Object> data;

    /**
     * Tên file để tracking
     */
    @NotBlank(message = "Name is required")
    private String name;
}
