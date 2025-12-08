package com.dinhhuan.loyaltyservice.controller;

import com.dinhhuan.loyaltyservice.dto.request.CreateRewardRequest;
import com.dinhhuan.loyaltyservice.dto.response.ApiResponse;
import com.dinhhuan.loyaltyservice.dto.response.CreateRewardResponse;
import com.dinhhuan.loyaltyservice.dto.response.RewardResponse;
import com.dinhhuan.loyaltyservice.service.BlockchainService;
import com.dinhhuan.loyaltyservice.service.IPFSService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller quản lý Rewards
 * 
 * Cung cấp các API:
 * - Tạo reward mới (Admin)
 * - Lấy thông tin reward
 * - Update reward metadata/image
 * 
 * Rewards là các phần thưởng mà khách hàng có thể đổi bằng token.
 * Mỗi reward có:
 * - ID: Unique identifier
 * - Cost: Số token cần để đổi
 * - Metadata: Tên, mô tả, điều khoản... (lưu trên IPFS)
 * - Image: Hình ảnh (lưu trên IPFS)
 */
@Slf4j
@RestController
@RequestMapping("/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final BlockchainService blockchainService;
    private final IPFSService ipfsService;

    /**
     * Tạo reward mới
     * 
     * Flow:
     * 1. Upload metadata JSON lên IPFS
     * 2. Upload image lên IPFS (nếu có)
     * 3. Gọi setRewardCost() trên smart contract
     * 4. Gọi setRewardMetadata() với CID
     * 5. Gọi setRewardImage() với CID (nếu có)
     * 
     * POST /api/v1/rewards
     * Body: { "rewardId": 1, "cost": 500, "metadata": {...}, "imageBase64": "..." }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CreateRewardResponse>> createReward(
            @Valid @RequestBody CreateRewardRequest request) {

        log.info("Creating reward: ID={}, Cost={}", request.getRewardId(), request.getCost());

        Map<String, String> transactions = new HashMap<>();
        String metadataCid = null;
        String imageCid = null;

        // 1. Upload metadata to IPFS
        metadataCid = ipfsService.uploadJson(
                request.getMetadata(),
                "reward_metadata_" + request.getRewardId());
        log.info("Metadata uploaded to IPFS: {}", metadataCid);

        // 2. Upload image to IPFS (if provided)
        if (request.getImageBase64() != null && !request.getImageBase64().isEmpty()) {
            imageCid = ipfsService.uploadBase64Image(
                    request.getImageBase64(),
                    "reward_image_" + request.getRewardId());
            log.info("Image uploaded to IPFS: {}", imageCid);
        }

        // 3. Set reward cost on blockchain
        TransactionReceipt costReceipt = blockchainService.setRewardCost(
                request.getRewardId(),
                request.getCost());
        transactions.put("setRewardCost", costReceipt.getTransactionHash());

        // 4. Set metadata CID on blockchain
        TransactionReceipt metadataReceipt = blockchainService.setRewardMetadata(
                request.getRewardId(),
                metadataCid);
        transactions.put("setRewardMetadata", metadataReceipt.getTransactionHash());

        // 5. Set image CID on blockchain (if uploaded)
        if (imageCid != null) {
            TransactionReceipt imageReceipt = blockchainService.setRewardImage(
                    request.getRewardId(),
                    imageCid);
            transactions.put("setRewardImage", imageReceipt.getTransactionHash());
        }

        // Build response
        CreateRewardResponse.IPFSInfo ipfsInfo = CreateRewardResponse.IPFSInfo.builder()
                .metadataCid(metadataCid)
                .metadataUrl(ipfsService.getGatewayUrl(metadataCid))
                .imageCid(imageCid)
                .imageUrl(imageCid != null ? ipfsService.getGatewayUrl(imageCid) : null)
                .build();

        CreateRewardResponse response = CreateRewardResponse.builder()
                .rewardId(request.getRewardId())
                .transactions(transactions)
                .ipfs(ipfsInfo)
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "Reward created successfully"));
    }

    /**
     * Lấy thông tin reward
     * 
     * GET /api/v1/rewards/{rewardId}
     */
    @GetMapping("/{rewardId}")
    public ResponseEntity<ApiResponse<RewardResponse>> getReward(@PathVariable Long rewardId) {
        log.debug("Getting reward: {}", rewardId);

        BigDecimal cost = blockchainService.getRewardCost(rewardId);

        if (cost.compareTo(BigDecimal.ZERO) == 0) {
            return ResponseEntity.notFound().build();
        }

        String metadataCid = blockchainService.getRewardMetadata(rewardId);
        String imageCid = blockchainService.getRewardImage(rewardId);

        // Fetch metadata from IPFS (optional - có thể bỏ qua để tiết kiệm time)
        Map<String, Object> metadata = null;
        if (metadataCid != null && !metadataCid.isEmpty()) {
            try {
                metadata = ipfsService.getJsonAsMap(metadataCid);
            } catch (Exception e) {
                log.warn("Failed to fetch metadata from IPFS: {}", e.getMessage());
            }
        }

        RewardResponse response = RewardResponse.builder()
                .rewardId(rewardId)
                .cost(cost.longValue())
                .metadataCid(metadataCid)
                .imageCid(imageCid)
                .metadata(metadata)
                .imageUrl(imageCid != null && !imageCid.isEmpty()
                        ? ipfsService.getGatewayUrl(imageCid)
                        : null)
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Lấy chi phí (số token) để đổi reward
     * 
     * GET /api/v1/rewards/{rewardId}/cost
     */
    @GetMapping("/{rewardId}/cost")
    public ResponseEntity<ApiResponse<Long>> getRewardCost(@PathVariable Long rewardId) {
        BigDecimal cost = blockchainService.getRewardCost(rewardId);
        return ResponseEntity.ok(ApiResponse.success(cost.longValue()));
    }

    /**
     * Cập nhật chi phí reward
     * 
     * PUT /api/v1/rewards/{rewardId}/cost
     */
    @PutMapping("/{rewardId}/cost")
    public ResponseEntity<ApiResponse<String>> updateRewardCost(
            @PathVariable Long rewardId,
            @RequestParam Long cost) {

        log.info("Updating reward {} cost to {}", rewardId, cost);

        TransactionReceipt receipt = blockchainService.setRewardCost(rewardId, cost);

        return ResponseEntity.ok(ApiResponse.success(
                receipt.getTransactionHash(),
                "Reward cost updated successfully"));
    }

    /**
     * Cập nhật metadata của reward
     * 
     * PUT /api/v1/rewards/{rewardId}/metadata
     */
    @PutMapping("/{rewardId}/metadata")
    public ResponseEntity<ApiResponse<String>> updateRewardMetadata(
            @PathVariable Long rewardId,
            @RequestBody Map<String, Object> metadata) {

        log.info("Updating reward {} metadata", rewardId);

        // Upload new metadata to IPFS
        String cid = ipfsService.uploadJson(metadata, "reward_metadata_" + rewardId + "_updated");

        // Update on blockchain
        TransactionReceipt receipt = blockchainService.setRewardMetadata(rewardId, cid);

        return ResponseEntity.ok(ApiResponse.success(
                receipt.getTransactionHash(),
                "Reward metadata updated. New CID: " + cid));
    }
}
