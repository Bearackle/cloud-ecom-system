package com.dinhhuan.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO để update wallet address cho user
 *
 * Được gọi từ frontend sau khi user kết nối MetaMask
 */
@Getter
@Setter
public class UpdateWalletRequest {
    private String walletAddress;
}

