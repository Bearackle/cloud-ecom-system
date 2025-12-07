package com.dinhhuan.loyaltyservice.service;

import com.dinhhuan.loyaltyservice.config.BlockchainConfig;
import com.dinhhuan.loyaltyservice.contract.LoyaltyManagerContract;
import com.dinhhuan.loyaltyservice.contract.LoyaltyTokenContract;
import com.dinhhuan.loyaltyservice.exception.BlockchainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Service tương tác với Blockchain qua Web3j
 * 
 * Service này đóng vai trò cầu nối giữa backend và smart contracts.
 * Tất cả các tương tác với blockchain đều đi qua đây.
 * 
 * Có 2 loại operations:
 * 1. READ (View functions) - Không tốn gas, kết quả trả về ngay
 * 2. WRITE (State-changing) - Tốn gas, cần đợi transaction được mine
 * 
 * Flow của một WRITE operation:
 * 1. Backend tạo transaction với data cần gửi
 * 2. Transaction được ký bằng private key của owner
 * 3. Transaction được broadcast lên network
 * 4. Miners/Validators xác nhận transaction
 * 5. Transaction được include vào block
 * 6. Backend nhận TransactionReceipt
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final Web3j web3j;
    private final LoyaltyTokenContract loyaltyTokenContract;
    private final LoyaltyManagerContract loyaltyManagerContract;
    private final BlockchainConfig blockchainConfig;

    /**
     * Số decimals của token (18 cho ERC20 standard)
     * 1 token = 10^18 wei
     */
    private static final BigInteger DECIMALS = BigInteger.valueOf(18);
    private static final BigInteger DECIMAL_FACTOR = BigInteger.TEN.pow(18);

    // =====================================================
    // READ OPERATIONS (View functions - Không tốn gas)
    // =====================================================

    /**
     * Kiểm tra khách hàng đã đăng ký chưa
     * 
     * @param customerAddress Địa chỉ ví cần kiểm tra
     * @return true nếu đã đăng ký, false nếu chưa
     */
    public boolean isCustomerRegistered(String customerAddress) {
        try {
            log.debug("Checking if customer {} is registered", customerAddress);
            Boolean result = loyaltyManagerContract.isCustomerRegistered(customerAddress).send();
            log.debug("Customer {} registered: {}", customerAddress, result);
            return result != null && result;
        } catch (Exception e) {
            log.error("Error checking customer registration for {}: {}", customerAddress, e.getMessage());
            throw new BlockchainException("Failed to check customer registration", e);
        }
    }

    /**
     * Lấy số dư token của một địa chỉ
     * 
     * @param customerAddress Địa chỉ ví cần kiểm tra
     * @return Số dư token (đã chia cho 10^18)
     */
    public BigDecimal getTokenBalance(String customerAddress) {
        try {
            log.debug("Getting token balance for {}", customerAddress);
            BigInteger balanceWei = loyaltyTokenContract.balanceOf(customerAddress).send();

            // Chuyển từ Wei sang Token (chia 10^18)
            BigDecimal balance = new BigDecimal(balanceWei)
                    .divide(new BigDecimal(DECIMAL_FACTOR), 18, RoundingMode.HALF_UP);

            log.debug("Token balance for {}: {} tokens ({} wei)", customerAddress, balance, balanceWei);
            return balance;
        } catch (Exception e) {
            log.error("Error getting token balance for {}: {}", customerAddress, e.getMessage());
            throw new BlockchainException("Failed to get token balance", e);
        }
    }

    /**
     * Lấy chi phí (số token) để đổi một reward
     * 
     * @param rewardId ID của reward
     * @return Số token cần (đã chia cho 10^18)
     */
    public BigDecimal getRewardCost(Long rewardId) {
        try {
            log.debug("Getting reward cost for reward {}", rewardId);
            BigInteger costWei = loyaltyManagerContract.getRewardCost(BigInteger.valueOf(rewardId)).send();

            BigDecimal cost = new BigDecimal(costWei)
                    .divide(new BigDecimal(DECIMAL_FACTOR), 18, RoundingMode.HALF_UP);

            log.debug("Reward {} cost: {} tokens", rewardId, cost);
            return cost;
        } catch (Exception e) {
            log.error("Error getting reward cost for {}: {}", rewardId, e.getMessage());
            throw new BlockchainException("Failed to get reward cost", e);
        }
    }

    /**
     * Lấy IPFS CID của reward metadata
     * 
     * @param rewardId ID của reward
     * @return IPFS CID string
     */
    public String getRewardMetadata(Long rewardId) {
        try {
            log.debug("Getting reward metadata for reward {}", rewardId);
            String cid = loyaltyManagerContract.getRewardMetadata(BigInteger.valueOf(rewardId)).send();
            log.debug("Reward {} metadata CID: {}", rewardId, cid);
            return cid;
        } catch (Exception e) {
            log.error("Error getting reward metadata for {}: {}", rewardId, e.getMessage());
            throw new BlockchainException("Failed to get reward metadata", e);
        }
    }

    /**
     * Lấy IPFS CID của reward image
     * 
     * @param rewardId ID của reward
     * @return IPFS CID string
     */
    public String getRewardImage(Long rewardId) {
        try {
            log.debug("Getting reward image for reward {}", rewardId);
            String cid = loyaltyManagerContract.getRewardImage(BigInteger.valueOf(rewardId)).send();
            log.debug("Reward {} image CID: {}", rewardId, cid);
            return cid;
        } catch (Exception e) {
            log.error("Error getting reward image for {}: {}", rewardId, e.getMessage());
            throw new BlockchainException("Failed to get reward image", e);
        }
    }

    /**
     * Lấy số lượng certificate của một khách hàng
     * 
     * @param customerAddress Địa chỉ ví
     * @return Số lượng certificate
     */
    public int getCertificateCount(String customerAddress) {
        try {
            log.debug("Getting certificate count for {}", customerAddress);
            BigInteger count = loyaltyManagerContract.getCertificateCount(customerAddress).send();
            log.debug("Customer {} has {} certificates", customerAddress, count);
            return count.intValue();
        } catch (Exception e) {
            log.error("Error getting certificate count for {}: {}", customerAddress, e.getMessage());
            throw new BlockchainException("Failed to get certificate count", e);
        }
    }

    // =====================================================
    // WRITE OPERATIONS (State-changing - Tốn gas)
    // =====================================================

    /**
     * Đăng ký khách hàng mới vào hệ thống
     * 
     * Chỉ owner mới có quyền gọi function này.
     * Sau khi đăng ký, khách hàng mới có thể nhận và đổi token.
     * 
     * @param customerAddress Địa chỉ ví khách hàng
     * @return TransactionReceipt chứa thông tin transaction
     */
    public TransactionReceipt registerCustomer(String customerAddress) {
        try {
            log.info("Registering customer: {}", customerAddress);

            // Kiểm tra đã đăng ký chưa
            if (isCustomerRegistered(customerAddress)) {
                throw new BlockchainException("Customer already registered: " + customerAddress);
            }

            // Gọi contract - Đợi transaction được mine
            TransactionReceipt receipt = loyaltyManagerContract.registerCustomer(customerAddress).send();

            log.info("Customer {} registered successfully. TxHash: {}, Block: {}",
                    customerAddress, receipt.getTransactionHash(), receipt.getBlockNumber());

            return receipt;
        } catch (BlockchainException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error registering customer {}: {}", customerAddress, e.getMessage());
            throw new BlockchainException("Failed to register customer", e);
        }
    }

    /**
     * Phát hành token cho khách hàng
     * 
     * Được gọi sau khi khách hàng hoàn thành mua hàng.
     * 
     * @param customerAddress Địa chỉ ví khách hàng
     * @param amount          Số token cần phát hành (chưa nhân 10^18)
     * @return TransactionReceipt
     */
    public TransactionReceipt issueTokens(String customerAddress, Long amount) {
        try {
            log.info("Issuing {} tokens to customer {}", amount, customerAddress);

            // Kiểm tra khách hàng đã đăng ký chưa
            if (!isCustomerRegistered(customerAddress)) {
                throw new BlockchainException("Customer not registered: " + customerAddress);
            }

            // Chuyển amount sang Wei (nhân 10^18)
            BigInteger amountWei = BigInteger.valueOf(amount).multiply(DECIMAL_FACTOR);

            log.debug("Amount in wei: {}", amountWei);

            // Gọi contract
            TransactionReceipt receipt = loyaltyManagerContract.issueTokens(customerAddress, amountWei).send();

            log.info("Issued {} tokens to {}. TxHash: {}, Block: {}",
                    amount, customerAddress, receipt.getTransactionHash(), receipt.getBlockNumber());

            return receipt;
        } catch (BlockchainException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error issuing tokens to {}: {}", customerAddress, e.getMessage());
            throw new BlockchainException("Failed to issue tokens", e);
        }
    }

    /**
     * Set chi phí cho một reward
     * 
     * @param rewardId ID của reward
     * @param cost     Số token cần để đổi (chưa nhân 10^18)
     * @return TransactionReceipt
     */
    public TransactionReceipt setRewardCost(Long rewardId, Long cost) {
        try {
            log.info("Setting reward {} cost to {} tokens", rewardId, cost);

            // Chuyển cost sang Wei
            BigInteger costWei = BigInteger.valueOf(cost).multiply(DECIMAL_FACTOR);

            TransactionReceipt receipt = loyaltyManagerContract
                    .setRewardCost(BigInteger.valueOf(rewardId), costWei).send();

            log.info("Reward {} cost set to {} tokens. TxHash: {}",
                    rewardId, cost, receipt.getTransactionHash());

            return receipt;
        } catch (Exception e) {
            log.error("Error setting reward cost for {}: {}", rewardId, e.getMessage());
            throw new BlockchainException("Failed to set reward cost", e);
        }
    }

    /**
     * Set IPFS CID cho reward metadata
     * 
     * @param rewardId ID của reward
     * @param ipfsCid  IPFS CID của metadata JSON
     * @return TransactionReceipt
     */
    public TransactionReceipt setRewardMetadata(Long rewardId, String ipfsCid) {
        try {
            log.info("Setting reward {} metadata CID: {}", rewardId, ipfsCid);

            TransactionReceipt receipt = loyaltyManagerContract
                    .setRewardMetadata(BigInteger.valueOf(rewardId), ipfsCid).send();

            log.info("Reward {} metadata set. TxHash: {}", rewardId, receipt.getTransactionHash());

            return receipt;
        } catch (Exception e) {
            log.error("Error setting reward metadata for {}: {}", rewardId, e.getMessage());
            throw new BlockchainException("Failed to set reward metadata", e);
        }
    }

    /**
     * Set IPFS CID cho reward image
     * 
     * @param rewardId ID của reward
     * @param ipfsCid  IPFS CID của image
     * @return TransactionReceipt
     */
    public TransactionReceipt setRewardImage(Long rewardId, String ipfsCid) {
        try {
            log.info("Setting reward {} image CID: {}", rewardId, ipfsCid);

            TransactionReceipt receipt = loyaltyManagerContract
                    .setRewardImage(BigInteger.valueOf(rewardId), ipfsCid).send();

            log.info("Reward {} image set. TxHash: {}", rewardId, receipt.getTransactionHash());

            return receipt;
        } catch (Exception e) {
            log.error("Error setting reward image for {}: {}", rewardId, e.getMessage());
            throw new BlockchainException("Failed to set reward image", e);
        }
    }

    /**
     * Phát hành certificate cho khách hàng
     * 
     * Được gọi sau khi khách hàng đổi thưởng thành công.
     * Certificate chứa thông tin voucher và được lưu on-chain.
     * 
     * @param customerAddress Địa chỉ ví khách hàng
     * @param certificateCid  IPFS CID của certificate
     * @return TransactionReceipt
     */
    public TransactionReceipt issueCertificate(String customerAddress, String certificateCid) {
        try {
            log.info("Issuing certificate to {}: {}", customerAddress, certificateCid);

            // Kiểm tra khách hàng đã đăng ký chưa
            if (!isCustomerRegistered(customerAddress)) {
                throw new BlockchainException("Customer not registered: " + customerAddress);
            }

            TransactionReceipt receipt = loyaltyManagerContract
                    .issueCertificate(customerAddress, certificateCid).send();

            log.info("Certificate issued to {}. TxHash: {}", customerAddress, receipt.getTransactionHash());

            return receipt;
        } catch (BlockchainException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error issuing certificate to {}: {}", customerAddress, e.getMessage());
            throw new BlockchainException("Failed to issue certificate", e);
        }
    }

    // =====================================================
    // UTILITY METHODS
    // =====================================================

    /**
     * Kiểm tra kết nối với Ethereum node
     * 
     * @return true nếu connected
     */
    public boolean isConnected() {
        try {
            web3j.web3ClientVersion().send();
            return true;
        } catch (Exception e) {
            log.error("Web3j not connected: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Lấy block number hiện tại
     * 
     * @return Block number
     */
    public Long getCurrentBlockNumber() {
        try {
            return web3j.ethBlockNumber().send().getBlockNumber().longValue();
        } catch (Exception e) {
            log.error("Error getting current block number: {}", e.getMessage());
            throw new BlockchainException("Failed to get current block number", e);
        }
    }

    /**
     * Lấy địa chỉ contract LoyaltyToken
     */
    public String getLoyaltyTokenAddress() {
        return blockchainConfig.getContracts().getLoyaltyToken();
    }

    /**
     * Lấy địa chỉ contract LoyaltyManager
     */
    public String getLoyaltyManagerAddress() {
        return blockchainConfig.getContracts().getLoyaltyManager();
    }
}
