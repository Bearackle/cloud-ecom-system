package com.dinhhuan.loyaltyservice.controller;

import com.dinhhuan.loyaltyservice.dto.request.IssueCertificateRequest;
import com.dinhhuan.loyaltyservice.dto.request.VerifyCertificateRequest;
import com.dinhhuan.loyaltyservice.dto.response.ApiResponse;
import com.dinhhuan.loyaltyservice.dto.response.CertificateResponse;
import com.dinhhuan.loyaltyservice.dto.response.VerifyCertificateResponse;
import com.dinhhuan.loyaltyservice.service.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller quản lý Certificates (Vouchers)
 * 
 * Cung cấp các API:
 * - Phát hành certificate sau khi đổi thưởng
 * - Verify certificate
 * - Mark certificate as used
 * - Lấy danh sách certificates của khách hàng
 * 
 * Certificate Workflow:
 * 1. Khách hàng đổi thưởng trên frontend (gọi redeemReward qua MetaMask)
 * 2. Frontend gửi request đến /api/v1/certificates/issue
 * 3. Backend tạo certificate PDF với QR code
 * 4. Upload PDF lên IPFS
 * 5. Gọi issueCertificate() trên blockchain
 * 6. Trả về voucher code cho khách
 * 7. Khách hàng sử dụng voucher code tại cửa hàng
 * 8. Cửa hàng verify và mark as used
 */
@Slf4j
@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    /**
     * Phát hành certificate sau khi đổi thưởng
     * 
     * Được gọi từ frontend sau khi user gọi redeemReward thành công.
     * 
     * POST /api/v1/certificates/issue
     * Body: {
     * "customerAddress": "0x...",
     * "rewardId": 1,
     * "rewardName": "Voucher giảm 10%",
     * "redeemTxHash": "0x..."
     * }
     */
    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<CertificateResponse>> issueCertificate(
            @Valid @RequestBody IssueCertificateRequest request) {

        log.info("Issuing certificate for customer {} - Reward {} ({})",
                request.getCustomerAddress(), request.getRewardId(), request.getRewardName());

        CertificateResponse certificate = certificateService.issueCertificate(request);

        return ResponseEntity.ok(ApiResponse.success(certificate, "Certificate issued successfully"));
    }

    /**
     * Verify certificate
     * 
     * Cửa hàng sử dụng API này để kiểm tra voucher code có hợp lệ không.
     * 
     * POST /api/v1/certificates/verify
     * Body: { "customerAddress": "0x...", "voucherCode": "VOUCHER-ABC123" }
     */
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyCertificateResponse>> verifyCertificate(
            @Valid @RequestBody VerifyCertificateRequest request) {

        log.info("Verifying certificate: {} for customer {}",
                request.getVoucherCode(), request.getCustomerAddress());

        VerifyCertificateResponse result = certificateService.verifyCertificate(
                request.getCustomerAddress(),
                request.getVoucherCode());

        if (result.isValid()) {
            return ResponseEntity.ok(ApiResponse.success(result, "Certificate is valid"));
        } else {
            return ResponseEntity.ok(ApiResponse.success(result, "Certificate is invalid"));
        }
    }

    /**
     * Verify certificate (GET version - đơn giản hơn)
     * 
     * GET
     * /api/v1/certificates/verify?customerAddress=0x...&voucherCode=VOUCHER-ABC123
     */
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyCertificateResponse>> verifyCertificateGet(
            @RequestParam String customerAddress,
            @RequestParam String voucherCode) {

        log.info("Verifying certificate (GET): {} for customer {}", voucherCode, customerAddress);

        VerifyCertificateResponse result = certificateService.verifyCertificate(
                customerAddress,
                voucherCode);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * Mark certificate as redeemed (đã sử dụng)
     * 
     * Cửa hàng gọi API này sau khi khách hàng sử dụng voucher.
     * 
     * POST /api/v1/certificates/{voucherCode}/redeem
     */
    @PostMapping("/{voucherCode}/redeem")
    public ResponseEntity<ApiResponse<Boolean>> markAsRedeemed(@PathVariable String voucherCode) {
        log.info("Marking certificate as redeemed: {}", voucherCode);

        boolean success = certificateService.markAsRedeemed(voucherCode);

        return ResponseEntity.ok(ApiResponse.success(success, "Certificate marked as redeemed"));
    }

    /**
     * Lấy tất cả certificates của khách hàng
     * 
     * GET /api/v1/certificates/customer/{walletAddress}
     */
    @GetMapping("/customer/{walletAddress}")
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> getCustomerCertificates(
            @PathVariable String walletAddress) {

        log.debug("Getting certificates for customer: {}", walletAddress);

        List<CertificateResponse> certificates = certificateService.getCustomerCertificates(walletAddress);

        return ResponseEntity.ok(ApiResponse.success(certificates));
    }

    /**
     * Lấy certificates chưa sử dụng của khách hàng
     * 
     * GET /api/v1/certificates/customer/{walletAddress}/unused
     */
    @GetMapping("/customer/{walletAddress}/unused")
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> getUnusedCertificates(
            @PathVariable String walletAddress) {

        log.debug("Getting unused certificates for customer: {}", walletAddress);

        List<CertificateResponse> certificates = certificateService.getUnusedCertificates(walletAddress);

        return ResponseEntity.ok(ApiResponse.success(certificates));
    }

    /**
     * Lấy certificate theo voucher code
     * 
     * GET /api/v1/certificates/{voucherCode}
     */
    @GetMapping("/{voucherCode}")
    public ResponseEntity<ApiResponse<CertificateResponse>> getCertificate(
            @PathVariable String voucherCode) {

        log.debug("Getting certificate: {}", voucherCode);

        // Tìm trong database và trả về
        List<CertificateResponse> all = certificateService.getCustomerCertificates("");

        // Tìm theo voucher code
        for (CertificateResponse cert : all) {
            if (cert.getVoucherCode().equals(voucherCode)) {
                return ResponseEntity.ok(ApiResponse.success(cert));
            }
        }

        return ResponseEntity.notFound().build();
    }
}
