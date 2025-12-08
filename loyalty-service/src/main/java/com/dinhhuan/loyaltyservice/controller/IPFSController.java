package com.dinhhuan.loyaltyservice.controller;

import com.dinhhuan.loyaltyservice.dto.request.UploadJsonRequest;
import com.dinhhuan.loyaltyservice.dto.response.ApiResponse;
import com.dinhhuan.loyaltyservice.dto.response.IPFSUploadResponse;
import com.dinhhuan.loyaltyservice.service.IPFSService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * REST Controller quản lý IPFS operations
 * 
 * Cung cấp các API:
 * - Upload JSON metadata lên IPFS
 * - Upload file (image/PDF) lên IPFS
 * - Retrieve content từ IPFS
 * 
 * IPFS (InterPlanetary File System):
 * - Lưu trữ phi tập trung (decentralized storage)
 * - Content-addressable: File được định danh bằng hash của nội dung (CID)
 * - Immutable: Không thể sửa file đã upload
 * 
 * Pinata:
 * - Dịch vụ IPFS pinning
 * - Đảm bảo file luôn available
 * - Cung cấp gateway để truy cập file qua HTTP
 */
@Slf4j
@RestController
@RequestMapping("/ipfs")
@RequiredArgsConstructor
public class IPFSController {

    private final IPFSService ipfsService;

    /**
     * Upload JSON data lên IPFS
     * 
     * Dùng để upload metadata của reward, certificate...
     * 
     * POST /api/v1/ipfs/upload/json
     * Body: { "data": {...}, "name": "reward_metadata_1" }
     */
    @PostMapping("/upload/json")
    public ResponseEntity<ApiResponse<IPFSUploadResponse>> uploadJson(
            @Valid @RequestBody UploadJsonRequest request) {

        log.info("Uploading JSON to IPFS: {}", request.getName());

        String cid = ipfsService.uploadJson(request.getData(), request.getName());

        IPFSUploadResponse response = IPFSUploadResponse.builder()
                .ipfsCid(cid)
                .ipfsUrl(ipfsService.getGatewayUrl(cid))
                .mimeType("application/json")
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "JSON uploaded successfully"));
    }

    /**
     * Upload file lên IPFS
     * 
     * Dùng để upload hình ảnh của reward, certificate PDF...
     * 
     * POST /api/v1/ipfs/upload/file
     * Form-data: file (binary), name (string)
     */
    @PostMapping(value = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<IPFSUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name) {

        log.info("Uploading file to IPFS: {}, size: {} bytes", name, file.getSize());

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File is empty"));
        }

        // Validate file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File size exceeds 10MB limit"));
        }

        String cid = ipfsService.uploadFile(file, name);

        IPFSUploadResponse response = IPFSUploadResponse.builder()
                .ipfsCid(cid)
                .ipfsUrl(ipfsService.getGatewayUrl(cid))
                .fileSize(file.getSize())
                .mimeType(file.getContentType())
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "File uploaded successfully"));
    }

    /**
     * Lấy JSON từ IPFS
     * 
     * GET /api/v1/ipfs/{cid}
     */
    @GetMapping("/{cid}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getJson(@PathVariable String cid) {
        log.debug("Getting JSON from IPFS: {}", cid);

        Map<String, Object> data = ipfsService.getJsonAsMap(cid);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * Lấy file bytes từ IPFS
     * 
     * GET /api/v1/ipfs/{cid}/file
     */
    @GetMapping("/{cid}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable String cid) {
        log.debug("Getting file from IPFS: {}", cid);

        byte[] data = ipfsService.getFile(cid);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

    /**
     * Kiểm tra CID có tồn tại không
     * 
     * GET /api/v1/ipfs/{cid}/verify
     */
    @GetMapping("/{cid}/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyCid(@PathVariable String cid) {
        log.debug("Verifying CID: {}", cid);

        boolean exists = ipfsService.verifyCid(cid);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    /**
     * Lấy gateway URL cho một CID
     * 
     * GET /api/v1/ipfs/{cid}/url
     */
    @GetMapping("/{cid}/url")
    public ResponseEntity<ApiResponse<String>> getGatewayUrl(@PathVariable String cid) {
        String url = ipfsService.getGatewayUrl(cid);
        return ResponseEntity.ok(ApiResponse.success(url));
    }
}
