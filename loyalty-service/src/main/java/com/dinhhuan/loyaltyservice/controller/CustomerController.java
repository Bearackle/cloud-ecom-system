package com.dinhhuan.loyaltyservice.controller;

import com.dinhhuan.loyaltyservice.dto.request.IssueTokensRequest;
import com.dinhhuan.loyaltyservice.dto.request.RegisterCustomerRequest;
import com.dinhhuan.loyaltyservice.dto.response.*;
import com.dinhhuan.loyaltyservice.entity.TokenTransaction;
import com.dinhhuan.loyaltyservice.repository.TokenTransactionRepository;
import com.dinhhuan.loyaltyservice.service.BlockchainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;

/**
 * REST Controller quản lý Customer
 * 
 * Cung cấp các API:
 * - Đăng ký khách hàng vào hệ thống loyalty
 * - Phát hành token cho khách hàng
 * - Lấy thông tin khách hàng
 * 
 * Authentication:
 * - Các API public (GET thông tin) không cần JWT
 * - Các API gọi từ service khác (POST) không cần JWT
 * - Các API gọi từ user qua API Gateway cần JWT (xử lý ở Gateway)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final BlockchainService blockchainService;
    private final TokenTransactionRepository tokenTransactionRepository;

    /**
     * Đăng ký khách hàng mới vào hệ thống loyalty
     * 
     * Flow:
     * 1. User kết nối ví MetaMask trên frontend
     * 2. Frontend gửi request đến backend với wallet address
     * 3. Backend gọi registerCustomer() trên smart contract
     * 4. Khách hàng được đăng ký và có thể nhận/đổi token
     * 
     * POST /api/v1/customers/register
     * Body: { "walletAddress": "0x..." }
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterCustomerResponse>> registerCustomer(
            @Valid @RequestBody RegisterCustomerRequest request) {

        log.info("Registering customer: {}", request.getWalletAddress());

        // Kiểm tra đã đăng ký chưa
        if (blockchainService.isCustomerRegistered(request.getWalletAddress())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Customer already registered"));
        }

        // Gọi blockchain để đăng ký
        TransactionReceipt receipt = blockchainService.registerCustomer(request.getWalletAddress());

        RegisterCustomerResponse response = RegisterCustomerResponse.builder()
                .walletAddress(request.getWalletAddress())
                .transactionHash(receipt.getTransactionHash())
                .blockNumber(receipt.getBlockNumber().longValue())
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "Customer registered successfully"));
    }

    /**
     * Phát hành token cho khách hàng
     * 
     * Được gọi từ Order Service sau khi khách hoàn thành mua hàng.
     * 
     * Flow:
     * 1. Khách hàng thanh toán thành công
     * 2. Order Service tính số token dựa trên giá trị đơn hàng
     * 3. Order Service gọi API này
     * 4. Backend gọi issueTokens() trên smart contract
     * 5. Khách nhận được token trong ví
     * 
     * POST /api/v1/customers/{walletAddress}/tokens/issue
     * Body: { "amount": 100, "orderId": "ORD-123", "reason": "PURCHASE" }
     */
    @PostMapping("/{walletAddress}/tokens/issue")
    public ResponseEntity<ApiResponse<IssueTokensResponse>> issueTokens(
            @PathVariable String walletAddress,
            @Valid @RequestBody IssueTokensRequest request) {

        log.info("Issuing {} tokens to customer {} for order {}",
                request.getAmount(), walletAddress, request.getOrderId());

        // Kiểm tra khách hàng đã đăng ký chưa
        if (!blockchainService.isCustomerRegistered(walletAddress)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Customer not registered. Please register first."));
        }

        // Kiểm tra order đã được thưởng chưa (tránh thưởng trùng)
        if (tokenTransactionRepository.existsByOrderId(request.getOrderId())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Tokens already issued for this order"));
        }

        // Gọi blockchain để phát hành token
        TransactionReceipt receipt = blockchainService.issueTokens(walletAddress, request.getAmount());

        // Lấy số dư mới
        BigDecimal newBalance = blockchainService.getTokenBalance(walletAddress);

        // Lưu transaction vào database
        TokenTransaction transaction = TokenTransaction.builder()
                .customerAddress(walletAddress)
                .amount(BigDecimal.valueOf(request.getAmount()))
                .transactionType("ISSUE")
                .orderId(request.getOrderId())
                .txHash(receipt.getTransactionHash())
                .blockNumber(receipt.getBlockNumber().longValue())
                .build();

        tokenTransactionRepository.save(transaction);

        IssueTokensResponse response = IssueTokensResponse.builder()
                .walletAddress(walletAddress)
                .tokensIssued(request.getAmount())
                .newBalance(newBalance)
                .transactionHash(receipt.getTransactionHash())
                .orderId(request.getOrderId())
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "Tokens issued successfully"));
    }

    /**
     * Lấy thông tin khách hàng
     * 
     * Trả về thông tin đăng ký, số dư token, số certificate...
     * 
     * GET /api/v1/customers/{walletAddress}
     */
    @GetMapping("/{walletAddress}")
    public ResponseEntity<ApiResponse<CustomerInfoResponse>> getCustomerInfo(
            @PathVariable String walletAddress) {

        log.debug("Getting info for customer: {}", walletAddress);

        boolean registered = blockchainService.isCustomerRegistered(walletAddress);
        BigDecimal balance = BigDecimal.ZERO;
        int certCount = 0;

        if (registered) {
            balance = blockchainService.getTokenBalance(walletAddress);
            certCount = blockchainService.getCertificateCount(walletAddress);
        }

        CustomerInfoResponse response = CustomerInfoResponse.builder()
                .walletAddress(walletAddress)
                .registered(registered)
                .tokenBalance(balance)
                .certificateCount(certCount)
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Kiểm tra khách hàng đã đăng ký chưa
     * 
     * GET /api/v1/customers/{walletAddress}/registered
     */
    @GetMapping("/{walletAddress}/registered")
    public ResponseEntity<ApiResponse<Boolean>> isRegistered(@PathVariable String walletAddress) {
        boolean registered = blockchainService.isCustomerRegistered(walletAddress);
        return ResponseEntity.ok(ApiResponse.success(registered));
    }

    /**
     * Lấy số dư token của khách hàng
     * 
     * GET /api/v1/customers/{walletAddress}/balance
     */
    @GetMapping("/{walletAddress}/balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getBalance(@PathVariable String walletAddress) {
        BigDecimal balance = blockchainService.getTokenBalance(walletAddress);
        return ResponseEntity.ok(ApiResponse.success(balance));
    }
}
