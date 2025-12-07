package com.dinhhuan.loyaltyservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Request DTO để phát hành certificate sau khi đổi thưởng
 * 
 * Workflow:
 * 1. Khách hàng đổi thưởng trên frontend (gọi redeemReward qua MetaMask)
 * 2. Frontend gửi request này đến backend
 * 3. Backend tạo certificate PDF, upload lên IPFS
 * 4. Backend gọi issueCertificate trên smart contract
 * 5. Trả về voucher code cho khách
 */
@Data
public class IssueCertificateRequest {

    /**
     * Địa chỉ ví khách hàng đã đổi thưởng
     */
    @NotBlank(message = "Customer address is required")
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Invalid Ethereum address format")
    private String customerAddress;

    /**
     * ID của reward đã đổi
     */
    @NotNull(message = "Reward ID is required")
    private Long rewardId;

    /**
     * Tên reward (để hiển thị trên certificate)
     */
    @NotBlank(message = "Reward name is required")
    private String rewardName;

    /**
     * Transaction hash của giao dịch redeemReward trên blockchain
     * Dùng để verify và tránh issue certificate trùng
     */
    private String redeemTxHash;
}
