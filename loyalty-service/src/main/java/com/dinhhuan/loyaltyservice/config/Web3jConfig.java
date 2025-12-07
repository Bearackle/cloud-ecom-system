package com.dinhhuan.loyaltyservice.config;

import com.dinhhuan. loyaltyservice.contract.LoyaltyManagerContract;
import com.dinhhuan. loyaltyservice.contract.LoyaltyTokenContract;
import lombok.RequiredArgsConstructor;
import lombok. extern.slf4j.Slf4j;
import org. springframework.context.annotation. Bean;
import org.springframework.context. annotation.Configuration;
import org.web3j.crypto. Credentials;
import org.web3j. protocol.Web3j;
import org. web3j.protocol.http.HttpService;
import org. web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

/**
 * Configuration class để tạo các Bean liên quan đến Web3j
 * 
 * Web3j là thư viện Java để tương tác với Ethereum blockchain. 
 * 
 * Luồng hoạt động:
 * 1.  Web3j kết nối với Ethereum node qua RPC URL
 * 2. Credentials chứa private key để ký transaction
 * 3. GasProvider cung cấp gas price và gas limit
 * 4. Contract instances được load từ địa chỉ contract
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class Web3jConfig {
    
    private final BlockchainConfig blockchainConfig;
    
    /**
     * Tạo Web3j instance để kết nối với Ethereum node
     * 
     * Web3j sử dụng JSON-RPC để giao tiếp với node. 
     * Có thể dùng Infura, Alchemy, hoặc node riêng.
     */
    @Bean
    public Web3j web3j() {
        String rpcUrl = blockchainConfig.getSepolia().getRpcUrl();
        log.info("Connecting to Ethereum node: {}", rpcUrl);
        
        // HttpService tạo HTTP connection đến RPC endpoint
        return Web3j.build(new HttpService(rpcUrl));
    }
    
    /**
     * Tạo Credentials từ private key
     * 
     * Credentials chứa:
     * - Private key: Dùng để ký transaction
     * - Public key: Derived từ private key
     * - Address: Derived từ public key (20 bytes cuối của Keccak256 hash)
     */
    @Bean
    public Credentials credentials() {
        String privateKey = blockchainConfig.getOwner().getPrivateKey();
        
        // Tạo Credentials từ private key (hex string)
        Credentials creds = Credentials. create(privateKey);
        
        log.info("Loaded owner wallet: {}", creds.getAddress());
        return creds;
    }
    
    /**
     * Tạo GasProvider để cung cấp gas price và gas limit
     * 
     * Gas trong Ethereum:
     * - Gas Price: Giá bạn sẵn sàng trả cho mỗi đơn vị gas (Gwei)
     * - Gas Limit: Số gas tối đa bạn sẵn sàng tiêu cho transaction
     * - Transaction Fee = Gas Used × Gas Price
     * 
     * Nếu gas price cao -> Transaction được xử lý nhanh hơn
     * Nếu gas limit thấp hơn gas cần thiết -> Transaction fail
     */
    @Bean
    public ContractGasProvider gasProvider() {
        // Chuyển Gwei sang Wei (1 Gwei = 10^9 Wei)
        BigInteger gasPrice = BigInteger.valueOf(
            blockchainConfig.getGas(). getPriceGwei() * 1_000_000_000L
        );
        BigInteger gasLimit = BigInteger.valueOf(blockchainConfig.getGas().getLimit());
        
        log.info("Gas configuration - Price: {} Gwei, Limit: {}", 
            blockchainConfig.getGas().getPriceGwei(), 
            blockchainConfig.getGas().getLimit());
        
        return new StaticGasProvider(gasPrice, gasLimit);
    }
    
    /**
     * Load LoyaltyToken contract instance
     * 
     * Đây là ERC20 token contract, chứa các function:
     * - balanceOf(address): Lấy số dư token
     * - transfer(to, amount): Chuyển token
     * - approve(spender, amount): Cho phép spender tiêu token
     * - mint(to, amount): Phát hành token mới (chỉ owner)
     */
    @Bean
    public LoyaltyTokenContract loyaltyTokenContract(Web3j web3j, Credentials credentials, 
                                                      ContractGasProvider gasProvider) {
        String contractAddress = blockchainConfig.getContracts().getLoyaltyToken();
        log.info("Loading LoyaltyToken contract at: {}", contractAddress);
        
        return LoyaltyTokenContract.load(contractAddress, web3j, credentials, gasProvider);
    }
    
    /**
     * Load LoyaltyManager contract instance
     * 
     * Đây là contract chứa business logic:
     * - registerCustomer(address): Đăng ký khách hàng
     * - issueTokens(address, amount): Phát hành token
     * - redeemReward(rewardId): Đổi thưởng (gọi từ frontend)
     * - setRewardCost/Metadata/Image: Quản lý reward
     * - issueCertificate(address, cid): Phát hành certificate
     */
    @Bean
    public LoyaltyManagerContract loyaltyManagerContract(Web3j web3j, Credentials credentials,
                                                          ContractGasProvider gasProvider) {
        String contractAddress = blockchainConfig. getContracts(). getLoyaltyManager();
        log. info("Loading LoyaltyManager contract at: {}", contractAddress);
        
        return LoyaltyManagerContract.load(contractAddress, web3j, credentials, gasProvider);
    }
}