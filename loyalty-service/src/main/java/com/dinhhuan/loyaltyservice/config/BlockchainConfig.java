package com.dinhhuan.loyaltyservice.config;

import lombok.Data;
import org. springframework.boot.context.properties.ConfigurationProperties;
import org.springframework. context.annotation.Configuration;

/**
 * Configuration class để bind các giá trị từ application.yml
 * 
 * Mapping với prefix "blockchain" trong application.yml
 * 
 * Ví dụ:
 * blockchain:
 *   sepolia:
 *     rpc-url: https://sepolia. infura.io/v3/xxx
 *   contracts:
 *     loyalty-token: 0x... 
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainConfig {
    
    /**
     * Cấu hình Sepolia testnet
     */
    private Sepolia sepolia;
    
    /**
     * Địa chỉ các smart contracts
     */
    private Contracts contracts;
    
    /**
     * Thông tin owner wallet
     */
    private Owner owner;
    
    /**
     * Cấu hình gas
     */
    private Gas gas;
    
    @Data
    public static class Sepolia {
        /**
         * RPC URL để kết nối với node Ethereum
         * Lấy từ Infura hoặc Alchemy
         */
        private String rpcUrl;
        
        /**
         * Chain ID của network
         * Sepolia = 11155111
         */
        private int chainId;
    }
    
    @Data
    public static class Contracts {
        /**
         * Địa chỉ LoyaltyToken contract (ERC20)
         */
        private String loyaltyToken;
        
        /**
         * Địa chỉ LoyaltyManager contract (Business logic)
         */
        private String loyaltyManager;
    }
    
    @Data
    public static class Owner {
        /**
         * Private key của owner wallet
         * QUAN TRỌNG: Giữ bí mật, không commit lên Git! 
         * 
         * Owner là địa chỉ có quyền:
         * - registerCustomer()
         * - issueTokens()
         * - setRewardCost(), setRewardMetadata(), setRewardImage()
         * - issueCertificate()
         */
        private String privateKey;
    }
    
    @Data
    public static class Gas {
        /**
         * Gas price tính bằng Gwei
         * 1 Gwei = 10^9 Wei
         */
        private long priceGwei;
        
        /**
         * Gas limit cho mỗi transaction
         */
        private long limit;
    }
}