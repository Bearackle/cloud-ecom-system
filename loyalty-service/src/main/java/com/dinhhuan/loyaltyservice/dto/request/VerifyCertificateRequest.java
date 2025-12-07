package com.dinhhuan.loyaltyservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Request DTO để verify certificate
 * 
 * Cửa hàng sử dụng API này để kiểm tra voucher code có hợp lệ không
 * trước khi áp dụng cho khách hàng.
 */
@Data
public class VerifyCertificateRequest {

    /**
     * Địa chỉ ví khách hàng sở hữu voucher
     */
    @NotBlank(message = "Customer address is required")
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Invalid Ethereum address format")
    private String customerAddress;

    /**
     * Mã voucher cần verify
     */
    @NotBlank(message = "Voucher code is required")
    private String voucherCode;
}
