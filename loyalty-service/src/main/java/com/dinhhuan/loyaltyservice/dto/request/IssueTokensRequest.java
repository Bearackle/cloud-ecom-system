package com.dinhhuan.loyaltyservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO để phát hành token cho khách hàng
 * 
 * Được gọi từ Order Service sau khi khách hàng hoàn thành mua hàng.
 * 
 * Workflow:
 * 1. Khách hàng thanh toán thành công
 * 2. Order Service tính số token dựa trên giá trị đơn hàng
 * 3. Order Service gọi API issueTokens với request này
 * 4. Loyalty Service gọi smart contract để mint token
 */
@Data
public class IssueTokensRequest {

    /**
     * Số lượng token cần phát hành
     * 
     * Ví dụ: Đơn hàng 1,000,000 VND với tỷ lệ 1 token/10,000 VND
     * => amount = 100 tokens
     */
    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be at least 1")
    private Long amount;

    /**
     * ID đơn hàng để tracking và tránh thưởng trùng lặp
     */
    @NotBlank(message = "Order ID is required")
    private String orderId;

    /**
     * Lý do phát hành token
     * Ví dụ: PURCHASE, REFERRAL, PROMOTION, BONUS
     */
    @NotBlank(message = "Reason is required")
    private String reason;
}
