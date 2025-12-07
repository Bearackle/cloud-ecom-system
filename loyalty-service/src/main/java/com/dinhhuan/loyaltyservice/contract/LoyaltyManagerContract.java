package com.dinhhuan.loyaltyservice.contract;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * Java Wrapper cho LoyaltyManager Smart Contract
 * 
 * Contract source: huyleuit/loyalty-token/contracts/LoyaltyManager. vy
 * 
 * Đây là contract chứa toàn bộ business logic của hệ thống loyalty:
 * - Quản lý khách hàng (đăng ký)
 * - Phát hành token
 * - Đổi thưởng
 * - Quản lý rewards và IPFS metadata
 * - Phát hành certificates
 */
public class LoyaltyManagerContract extends Contract {

    public static final String BINARY = "";

    protected LoyaltyManagerContract(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider gasProvider) {
        super(BINARY, contractAddress, web3j, credentials, gasProvider);
    }

    public static LoyaltyManagerContract load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider gasProvider) {
        return new LoyaltyManagerContract(contractAddress, web3j, credentials, gasProvider);
    }

    // ==================== READ FUNCTIONS (View - Không tốn gas)
    // ====================

    /**
     * Kiểm tra khách hàng đã đăng ký chưa
     * 
     * Vyper code:
     * 
     * @view
     * @external
     *           def isCustomerRegistered(_customer: address) -> bool:
     *           return self.registered_customers[_customer]
     */
    public RemoteFunctionCall<Boolean> isCustomerRegistered(String customerAddress) {
        final Function function = new Function(
                "isCustomerRegistered",
                Arrays.asList(new Address(customerAddress)),
                Arrays.asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    /**
     * Lấy chi phí (số token cần) để đổi một reward
     * 
     * Vyper code:
     * 
     * @view
     * @external
     *           def getRewardCost(_reward_id: uint256) -> uint256:
     *           return self.reward_costs[_reward_id]
     */
    public RemoteFunctionCall<BigInteger> getRewardCost(BigInteger rewardId) {
        final Function function = new Function(
                "getRewardCost",
                Arrays.asList(new Uint256(rewardId)),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    /**
     * Lấy IPFS CID của reward metadata (JSON chứa tên, mô tả, điều kiện...)
     * 
     * Vyper code:
     * 
     * @view
     * @external
     *           def getRewardMetadata(_reward_id: uint256) -> String[100]:
     *           return self.reward_metadata[_reward_id]
     */
    public RemoteFunctionCall<String> getRewardMetadata(BigInteger rewardId) {
        final Function function = new Function(
                "getRewardMetadata",
                Arrays.asList(new Uint256(rewardId)),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * Lấy IPFS CID của reward image
     */
    public RemoteFunctionCall<String> getRewardImage(BigInteger rewardId) {
        final Function function = new Function(
                "getRewardImage",
                Arrays.asList(new Uint256(rewardId)),
                Arrays.asList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * Lấy số lượng certificate của một khách hàng
     */
    public RemoteFunctionCall<BigInteger> getCertificateCount(String customerAddress) {
        final Function function = new Function(
                "getCertificateCount",
                Arrays.asList(new Address(customerAddress)),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    // ==================== WRITE FUNCTIONS (Tốn gas - Cần ký transaction)
    // ====================

    /**
     * Đăng ký khách hàng mới vào hệ thống
     * 
     * Chỉ owner mới có quyền gọi function này.
     * 
     * Vyper code:
     * 
     * @external
     *           def registerCustomer(_customer: address):
     *           assert msg.sender == self.owner, "Only owner"
     *           assert not self.registered_customers[_customer], "Already
     *           registered"
     *           self.registered_customers[_customer] = True
     *           log CustomerRegistered(_customer)
     * 
     * @param customerAddress Địa chỉ ví của khách hàng
     * @return TransactionReceipt chứa thông tin transaction sau khi được mine
     */
    public RemoteFunctionCall<TransactionReceipt> registerCustomer(String customerAddress) {
        final Function function = new Function(
                "registerCustomer",
                Arrays.asList(new Address(customerAddress)), // Input
                Collections.emptyList() // Không có output (void)
        );
        // executeRemoteCallTransaction: Gửi transaction, đợi mine, trả về receipt
        return executeRemoteCallTransaction(function);
    }

    /**
     * Phát hành token cho khách hàng
     * 
     * Thường được gọi sau khi khách hoàn thành mua hàng.
     * Backend tính toán số token dựa trên giá trị đơn hàng rồi gọi function này.
     * 
     * Vyper code:
     * 
     * @external
     *           def issueTokens(_customer: address, _amount: uint256):
     *           assert msg.sender == self.owner, "Only owner"
     *           assert self.registered_customers[_customer], "Customer not
     *           registered"
     *           LoyaltyToken(self.token_contract).mint(_customer, _amount)
     * 
     * @param customerAddress Địa chỉ ví khách hàng
     * @param amount          Số token phát hành (đã nhân 10^18)
     */
    public RemoteFunctionCall<TransactionReceipt> issueTokens(String customerAddress, BigInteger amount) {
        final Function function = new Function(
                "issueTokens",
                Arrays.asList(new Address(customerAddress), new Uint256(amount)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    /**
     * Set chi phí (số token) cho một reward
     * 
     * Vyper code:
     * 
     * @external
     *           def setRewardCost(_reward_id: uint256, _cost: uint256):
     *           assert msg.sender == self.owner, "Only owner"
     *           ...
     */
    public RemoteFunctionCall<TransactionReceipt> setRewardCost(BigInteger rewardId, BigInteger cost) {
        final Function function = new Function(
                "setRewardCost",
                Arrays.asList(new Uint256(rewardId), new Uint256(cost)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    /**
     * Set IPFS CID cho reward metadata
     * 
     * Metadata là JSON chứa: tên, mô tả, điều khoản, hạn sử dụng...
     * 
     * Vyper code:
     * 
     * @external
     *           def setRewardMetadata(_reward_id: uint256, _ipfs_cid: String[100]):
     *           assert msg.sender == self.owner, "Only owner"
     *           assert self.reward_costs[_reward_id] > 0, "Reward does not exist"
     *           self.reward_metadata[_reward_id] = _ipfs_cid
     *           log RewardMetadataSet(_reward_id, _ipfs_cid)
     */
    public RemoteFunctionCall<TransactionReceipt> setRewardMetadata(BigInteger rewardId, String ipfsCid) {
        final Function function = new Function(
                "setRewardMetadata",
                Arrays.asList(new Uint256(rewardId), new Utf8String(ipfsCid)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    /**
     * Set IPFS CID cho reward image
     */
    public RemoteFunctionCall<TransactionReceipt> setRewardImage(BigInteger rewardId, String ipfsCid) {
        final Function function = new Function(
                "setRewardImage",
                Arrays.asList(new Uint256(rewardId), new Utf8String(ipfsCid)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    /**
     * Phát hành certificate cho khách hàng sau khi đổi thưởng
     * 
     * Certificate chứa: mã voucher, thông tin reward, verification hash...
     * Được lưu trên IPFS và CID được lưu on-chain.
     * 
     * Vyper code:
     * 
     * @external
     *           def issueCertificate(_customer: address, _certificate_cid:
     *           String[100]):
     *           assert msg.sender == self.owner, "Only owner"
     *           assert self. registered_customers[_customer], "Customer not
     *           registered"
     *           self.customer_certificates[_customer].append(_certificate_cid)
     *           log CertificateIssued(_customer, _certificate_cid)
     */
    public RemoteFunctionCall<TransactionReceipt> issueCertificate(String customerAddress, String certificateCid) {
        final Function function = new Function(
                "issueCertificate",
                Arrays.asList(new Address(customerAddress), new Utf8String(certificateCid)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }
}