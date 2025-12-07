package com.dinhhuan.loyaltyservice.service;

import com.dinhhuan.loyaltyservice.dto.request.IssueCertificateRequest;
import com.dinhhuan.loyaltyservice.dto.response.CertificateResponse;
import com.dinhhuan.loyaltyservice.dto.response.VerifyCertificateResponse;
import com.dinhhuan.loyaltyservice.entity.Certificate;
import com.dinhhuan.loyaltyservice.exception.BusinessException;
import com.dinhhuan.loyaltyservice.repository.CertificateRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service quản lý Certificate (Voucher)
 * 
 * Certificate là chứng nhận khách hàng đã đổi thưởng thành công.
 * Chứa:
 * - Voucher code: Mã unique để sử dụng
 * - QR code: Scan để verify nhanh
 * - Verification hash: Để xác minh tính xác thực
 * 
 * Workflow phát hành certificate:
 * 1. Khách hàng đổi thưởng trên frontend (gọi redeemReward qua MetaMask)
 * 2. Frontend gửi request đến backend
 * 3. Backend tạo certificate PDF với QR code
 * 4. Upload PDF lên IPFS
 * 5. Gọi issueCertificate trên blockchain để lưu CID
 * 6. Trả về voucher code cho khách
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final BlockchainService blockchainService;
    private final IPFSService ipfsService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // =====================================================
    // CERTIFICATE GENERATION
    // =====================================================

    /**
     * Phát hành certificate cho khách hàng sau khi đổi thưởng
     * 
     * @param request Thông tin request
     * @return CertificateResponse
     */
    @Transactional
    public CertificateResponse issueCertificate(IssueCertificateRequest request) {
        log.info("Issuing certificate for customer {} - Reward {}",
                request.getCustomerAddress(), request.getRewardId());

        // 1. Generate voucher code
        String voucherCode = generateVoucherCode();
        log.debug("Generated voucher code: {}", voucherCode);

        // 2. Calculate verification hash
        String verificationHash = calculateVerificationHash(
                request.getCustomerAddress(),
                voucherCode);
        log.debug("Verification hash: {}", verificationHash);

        // 3. Create certificate metadata (JSON)
        Map<String, Object> certificateMetadata = Map.of(
                "voucherCode", voucherCode,
                "customerAddress", request.getCustomerAddress(),
                "rewardId", request.getRewardId(),
                "rewardName", request.getRewardName(),
                "verificationHash", verificationHash,
                "issuedAt", LocalDateTime.now().format(DATE_FORMATTER),
                "redeemTxHash", request.getRedeemTxHash() != null ? request.getRedeemTxHash() : "");

        // 4. Generate PDF with QR code
        byte[] pdfBytes = generateCertificatePDF(
                voucherCode,
                request.getRewardName(),
                request.getCustomerAddress(),
                verificationHash);
        log.debug("Generated PDF, size: {} bytes", pdfBytes.length);

        // 5. Upload PDF to IPFS
        String certificateCid = ipfsService.uploadBytes(
                pdfBytes,
                "certificate_" + voucherCode + ".pdf",
                "application/pdf");
        log.info("Certificate uploaded to IPFS: {}", certificateCid);

        // 6. Call blockchain to store certificate CID
        TransactionReceipt receipt = blockchainService.issueCertificate(
                request.getCustomerAddress(),
                certificateCid);

        // 7. Save to database
        Certificate certificate = Certificate.builder()
                .customerAddress(request.getCustomerAddress())
                .rewardId(request.getRewardId())
                .voucherCode(voucherCode)
                .ipfsCid(certificateCid)
                .verificationHash(verificationHash)
                .txHash(receipt.getTransactionHash())
                .redeemed(false)
                .build();

        certificateRepository.save(certificate);
        log.info("Certificate saved to database: {}", voucherCode);

        // 8. Build response
        return CertificateResponse.builder()
                .voucherCode(voucherCode)
                .ipfsCid(certificateCid)
                .ipfsUrl(ipfsService.getGatewayUrl(certificateCid))
                .verificationHash(verificationHash)
                .transactionHash(receipt.getTransactionHash())
                .rewardId(request.getRewardId())
                .rewardName(request.getRewardName())
                .redeemed(false)
                .createdAt(certificate.getCreatedAt())
                .build();
    }

    /**
     * Generate PDF certificate với QR code
     * 
     * @return byte[] của PDF
     */
    public byte[] generateCertificatePDF(String voucherCode, String rewardName,
            String customerAddress, String verificationHash) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A5.rotate());

            // Title
            Paragraph title = new Paragraph("LOYALTY REWARD CERTIFICATE")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.DARK_GRAY);
            document.add(title);

            // Reward name
            Paragraph reward = new Paragraph(rewardName)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10);
            document.add(reward);

            // Voucher code (large)
            Paragraph voucher = new Paragraph("Voucher Code: " + voucherCode)
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20)
                    .setFontColor(ColorConstants.BLUE);
            document.add(voucher);

            // QR Code
            byte[] qrCodeBytes = generateQRCode(voucherCode + "|" + verificationHash);
            Image qrImage = new Image(ImageDataFactory.create(qrCodeBytes))
                    .setWidth(120)
                    .setHeight(120)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setMarginTop(15);
            document.add(qrImage);

            // Customer address (truncated)
            String truncatedAddress = customerAddress.substring(0, 6) + "..." +
                    customerAddress.substring(customerAddress.length() - 4);
            Paragraph customer = new Paragraph("Customer: " + truncatedAddress)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(15);
            document.add(customer);

            // Issue date
            Paragraph date = new Paragraph("Issued: " + LocalDateTime.now().format(DATE_FORMATTER))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(date);

            // Verification hash (small)
            Paragraph hash = new Paragraph("Verify: " + verificationHash.substring(0, 16) + "...")
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY);
            document.add(hash);

            // Footer
            Paragraph footer = new Paragraph("This certificate is stored on IPFS and verified on Ethereum blockchain")
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10)
                    .setFontColor(ColorConstants.GRAY);
            document.add(footer);

            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating certificate PDF: {}", e.getMessage());
            throw new BusinessException("Failed to generate certificate PDF", e);
        }
    }

    /**
     * Generate QR code image
     * 
     * @param content Nội dung encode trong QR
     * @return byte[] của PNG image
     */
    public byte[] generateQRCode(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);

            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);

            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating QR code: {}", e.getMessage());
            throw new BusinessException("Failed to generate QR code", e);
        }
    }

    // =====================================================
    // VERIFICATION
    // =====================================================

    /**
     * Verify certificate
     * 
     * @param customerAddress Địa chỉ ví
     * @param voucherCode     Mã voucher
     * @return VerifyCertificateResponse
     */
    public VerifyCertificateResponse verifyCertificate(String customerAddress, String voucherCode) {
        log.info("Verifying certificate: {} for customer {}", voucherCode, customerAddress);

        Optional<Certificate> certOpt = certificateRepository.findByVoucherCode(voucherCode);

        if (certOpt.isEmpty()) {
            return VerifyCertificateResponse.builder()
                    .valid(false)
                    .invalidReason("Voucher code not found")
                    .build();
        }

        Certificate cert = certOpt.get();

        // Check owner
        if (!cert.getCustomerAddress().equalsIgnoreCase(customerAddress)) {
            return VerifyCertificateResponse.builder()
                    .valid(false)
                    .invalidReason("Voucher does not belong to this customer")
                    .build();
        }

        // Verify hash
        String expectedHash = calculateVerificationHash(customerAddress, voucherCode);
        if (!expectedHash.equals(cert.getVerificationHash())) {
            return VerifyCertificateResponse.builder()
                    .valid(false)
                    .invalidReason("Verification hash mismatch")
                    .build();
        }

        return VerifyCertificateResponse.builder()
                .valid(true)
                .redeemed(cert.isRedeemed())
                .voucherCode(voucherCode)
                .rewardId(cert.getRewardId())
                .customerAddress(customerAddress)
                .createdAt(cert.getCreatedAt())
                .redeemedAt(cert.getRedeemedAt())
                .build();
    }

    /**
     * Mark certificate as redeemed (đã sử dụng)
     * 
     * @param voucherCode Mã voucher
     * @return true nếu thành công
     */
    @Transactional
    public boolean markAsRedeemed(String voucherCode) {
        log.info("Marking certificate as redeemed: {}", voucherCode);

        Optional<Certificate> certOpt = certificateRepository.findByVoucherCode(voucherCode);

        if (certOpt.isEmpty()) {
            throw new BusinessException("Voucher code not found: " + voucherCode);
        }

        Certificate cert = certOpt.get();

        if (cert.isRedeemed()) {
            throw new BusinessException("Voucher already redeemed: " + voucherCode);
        }

        cert.setRedeemed(true);
        cert.setRedeemedAt(LocalDateTime.now());
        certificateRepository.save(cert);

        log.info("Certificate marked as redeemed: {}", voucherCode);
        return true;
    }

    /**
     * Lấy tất cả certificate của khách hàng
     */
    public java.util.List<CertificateResponse> getCustomerCertificates(String customerAddress) {
        return certificateRepository.findByCustomerAddress(customerAddress)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Lấy certificate chưa sử dụng của khách hàng
     */
    public java.util.List<CertificateResponse> getUnusedCertificates(String customerAddress) {
        return certificateRepository.findByCustomerAddressAndRedeemedFalse(customerAddress)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // UTILITY METHODS
    // =====================================================

    /**
     * Generate unique voucher code
     * Format: VOUCHER-XXXXXXXX (8 random alphanumeric)
     */
    public String generateVoucherCode() {
        String code;
        do {
            code = "VOUCHER-" + UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 8)
                    .toUpperCase();
        } while (certificateRepository.existsByVoucherCode(code));

        return code;
    }

    /**
     * Calculate verification hash
     * Hash = SHA256(customerAddress + voucherCode + timestamp)
     */
    public String calculateVerificationHash(String customerAddress, String voucherCode) {
        try {
            String data = customerAddress.toLowerCase() + voucherCode + System.currentTimeMillis();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new BusinessException("Failed to calculate verification hash", e);
        }
    }

    /**
     * Convert entity to response DTO
     */
    private CertificateResponse toResponse(Certificate cert) {
        return CertificateResponse.builder()
                .voucherCode(cert.getVoucherCode())
                .ipfsCid(cert.getIpfsCid())
                .ipfsUrl(ipfsService.getGatewayUrl(cert.getIpfsCid()))
                .verificationHash(cert.getVerificationHash())
                .transactionHash(cert.getTxHash())
                .rewardId(cert.getRewardId())
                .redeemed(cert.isRedeemed())
                .createdAt(cert.getCreatedAt())
                .build();
    }
}
