package com.dinhhuan.loyaltyservice.contract;

import org.web3j. abi.TypeReference;
import org.web3j. abi.datatypes.Address;
import org. web3j. abi.datatypes. Function;
import org. web3j. abi.datatypes. generated.Uint256;
import org. web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tx.Contract;
import org. web3j.tx.gas.ContractGasProvider;

import java.math. BigInteger;
import java.util.Arrays;

/**
 * Java Wrapper cho LoyaltyToken Smart Contract (ERC20)
 * 
 * Contract source: huyleuit/loyalty-token/contracts/LoyaltyToken. vy
 * 
 * Đây là token ERC20 đại diện cho điểm thưởng khách hàng thân thiết. 
 * 
 * Các function chính:
 * - balanceOf: Xem số dư token
 * - transfer: Chuyển token
 * - approve: Cho phép contract khác tiêu token
 * - mint: Phát hành token mới (chỉ owner)
 */
public class LoyaltyTokenContract extends Contract {
    
    // Binary không cần vì contract đã deploy
    public static final String BINARY = "";
    
    /**
     * Constructor - Không gọi trực tiếp, sử dụng static method load()
     */
    protected LoyaltyTokenContract(String contractAddress, Web3j web3j,
                                    Credentials credentials, ContractGasProvider gasProvider) {
        super(BINARY, contractAddress, web3j, credentials, gasProvider);
    }
    
    /**
     * Load contract instance từ địa chỉ đã deploy
     * 
     * @param contractAddress Địa chỉ contract trên blockchain
     * @param web3j Web3j instance để giao tiếp với node
     * @param credentials Private key của caller
     * @param gasProvider Gas configuration
     */
    public static LoyaltyTokenContract load(String contractAddress, Web3j web3j,
                                             Credentials credentials, ContractGasProvider gasProvider) {
        return new LoyaltyTokenContract(contractAddress, web3j, credentials, gasProvider);
    }
    
    // ==================== READ FUNCTIONS (View - Không tốn gas) ====================
    
    /**
     * Lấy số dư token của một địa chỉ
     * 
     * Tương ứng với Vyper code:
     * @view
     * @external
     * def balanceOf(_owner: address) -> uint256:
     *     return self. balanceOf[_owner]
     * 
     * @param owner Địa chỉ cần kiểm tra
     * @return Số dư token (đơn vị Wei, cần chia 10^18)
     */
    public RemoteFunctionCall<BigInteger> balanceOf(String owner) {
        // Tạo Function object với:
        // - Tên function: "balanceOf"
        // - Input parameters: một Address
        // - Output type: Uint256
        final Function function = new Function(
            "balanceOf",
            Arrays.asList(new Address(owner)),              // Input
            Arrays.asList(new TypeReference<Uint256>() {})  // Output
        );
        
        // executeRemoteCallSingleValueReturn: Gọi function và trả về giá trị đơn
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
    
    /**
     * Lấy tổng supply của token
     */
    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(
            "totalSupply",
            Arrays.asList(),                                // Không có input
            Arrays. asList(new TypeReference<Uint256>() {})
        );
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
    
    /**
     * Lấy số decimals (thường là 18 cho ERC20)
     */
    public RemoteFunctionCall<BigInteger> decimals() {
        final Function function = new Function(
            "decimals",
            Arrays. asList(),
            Arrays.asList(new TypeReference<Uint256>() {})
        );
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
    
    /**
     * Lấy allowance (số token mà spender được phép tiêu)
     */
    public RemoteFunctionCall<BigInteger> allowance(String owner, String spender) {
        final Function function = new Function(
            "allowance",
            Arrays.asList(new Address(owner), new Address(spender)),
            Arrays.asList(new TypeReference<Uint256>() {})
        );
        return executeRemoteCallSingleValueReturn(function, BigInteger. class);
    }
}