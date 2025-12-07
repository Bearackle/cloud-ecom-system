package com.dinhhuan.loyaltyservice.service;

import com.dinhhuan.loyaltyservice.config.PinataConfig;
import com.dinhhuan.loyaltyservice.entity.IPFSUpload;
import com.dinhhuan.loyaltyservice.exception.IPFSException;
import com.dinhhuan.loyaltyservice.repository.IPFSUploadRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Service tương tác với IPFS qua Pinata API
 * 
 * IPFS (InterPlanetary File System) là hệ thống lưu trữ phi tập trung.
 * Pinata là dịch vụ pinning giúp lưu trữ file trên IPFS dễ dàng qua REST API.
 * 
 * Workflow upload:
 * 1. Client gửi file/JSON đến backend
 * 2. Backend gọi Pinata API để upload lên IPFS
 * 3. Pinata trả về CID (Content Identifier)
 * 4. CID được lưu vào database và/hoặc blockchain
 * 5. File có thể được truy cập qua: gateway.pinata.cloud/ipfs/{CID}
 * 
 * CID là hash của content, nên:
 * - Cùng content -> Cùng CID (content-addressable)
 * - Thay đổi content -> CID khác
 * - Không thể sửa file đã upload (immutable)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IPFSService {

    private final PinataConfig pinataConfig;
    private final IPFSUploadRepository ipfsUploadRepository;
    private final ObjectMapper objectMapper;

    // Pinata API endpoints
    private static final String PINATA_PIN_JSON_URL = "https://api.pinata.cloud/pinning/pinJSONToIPFS";
    private static final String PINATA_PIN_FILE_URL = "https://api.pinata.cloud/pinning/pinFileToIPFS";

    // OkHttp client với timeout config
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    // =====================================================
    // UPLOAD OPERATIONS
    // =====================================================

    /**
     * Upload JSON data lên IPFS
     * 
     * Thường dùng để upload metadata của reward/certificate.
     * 
     * @param data Map chứa dữ liệu JSON
     * @param name Tên file để tracking
     * @return IPFS CID
     */
    public String uploadJson(Map<String, Object> data, String name) {
        try {
            log.info("Uploading JSON to IPFS: {}", name);

            // Tạo request body
            // Format: { "pinataContent": {...}, "pinataMetadata": { "name": "..." } }
            Map<String, Object> requestBody = Map.of(
                    "pinataContent", data,
                    "pinataMetadata", Map.of("name", name));

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            RequestBody body = RequestBody.create(
                    jsonBody,
                    MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(PINATA_PIN_JSON_URL)
                    .addHeader("Authorization", "Bearer " + pinataConfig.getSecretKey())
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    log.error("Pinata API error: {} - {}", response.code(), errorBody);
                    throw new IPFSException("Failed to upload JSON to IPFS: " + errorBody);
                }

                String responseBody = response.body().string();
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                String cid = jsonResponse.get("IpfsHash").asText();

                log.info("JSON uploaded successfully. CID: {}", cid);

                // Lưu record vào database
                saveUploadRecord(cid, "METADATA", name, (long) jsonBody.length(), "application/json");

                return cid;
            }
        } catch (IPFSException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error uploading JSON to IPFS: {}", e.getMessage());
            throw new IPFSException("Failed to upload JSON to IPFS", e);
        }
    }

    /**
     * Upload file lên IPFS
     * 
     * @param file MultipartFile từ request
     * @param name Tên file để tracking
     * @return IPFS CID
     */
    public String uploadFile(MultipartFile file, String name) {
        try {
            log.info("Uploading file to IPFS: {}, size: {} bytes", name, file.getSize());

            // Tạo multipart request body
            RequestBody fileBody = RequestBody.create(
                    file.getBytes(),
                    MediaType.parse(file.getContentType()));

            MultipartBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(), fileBody)
                    .addFormDataPart("pinataMetadata", objectMapper.writeValueAsString(Map.of("name", name)))
                    .build();

            Request request = new Request.Builder()
                    .url(PINATA_PIN_FILE_URL)
                    .addHeader("Authorization", "Bearer " + pinataConfig.getSecretKey())
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    log.error("Pinata API error: {} - {}", response.code(), errorBody);
                    throw new IPFSException("Failed to upload file to IPFS: " + errorBody);
                }

                String responseBody = response.body().string();
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                String cid = jsonResponse.get("IpfsHash").asText();

                log.info("File uploaded successfully. CID: {}", cid);

                // Lưu record vào database
                saveUploadRecord(cid, "IMAGE", name, file.getSize(), file.getContentType());

                return cid;
            }
        } catch (IPFSException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error uploading file to IPFS: {}", e.getMessage());
            throw new IPFSException("Failed to upload file to IPFS", e);
        }
    }

    /**
     * Upload bytes data lên IPFS
     * 
     * @param data     Byte array của file
     * @param name     Tên file
     * @param mimeType MIME type của file
     * @return IPFS CID
     */
    public String uploadBytes(byte[] data, String name, String mimeType) {
        try {
            log.info("Uploading bytes to IPFS: {}, size: {} bytes", name, data.length);

            RequestBody fileBody = RequestBody.create(data, MediaType.parse(mimeType));

            MultipartBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", name, fileBody)
                    .addFormDataPart("pinataMetadata", objectMapper.writeValueAsString(Map.of("name", name)))
                    .build();

            Request request = new Request.Builder()
                    .url(PINATA_PIN_FILE_URL)
                    .addHeader("Authorization", "Bearer " + pinataConfig.getSecretKey())
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    log.error("Pinata API error: {} - {}", response.code(), errorBody);
                    throw new IPFSException("Failed to upload bytes to IPFS: " + errorBody);
                }

                String responseBody = response.body().string();
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                String cid = jsonResponse.get("IpfsHash").asText();

                log.info("Bytes uploaded successfully. CID: {}", cid);

                // Lưu record vào database
                saveUploadRecord(cid, "CERTIFICATE", name, (long) data.length, mimeType);

                return cid;
            }
        } catch (IPFSException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error uploading bytes to IPFS: {}", e.getMessage());
            throw new IPFSException("Failed to upload bytes to IPFS", e);
        }
    }

    // =====================================================
    // RETRIEVE OPERATIONS
    // =====================================================

    /**
     * Lấy JSON từ IPFS
     * 
     * @param cid   IPFS CID
     * @param clazz Class để deserialize
     * @return Object của type T
     */
    public <T> T getJson(String cid, Class<T> clazz) {
        try {
            log.debug("Getting JSON from IPFS: {}", cid);

            String url = getGatewayUrl(cid);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IPFSException("Failed to get JSON from IPFS: " + response.code());
                }

                String responseBody = response.body().string();
                return objectMapper.readValue(responseBody, clazz);
            }
        } catch (IPFSException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting JSON from IPFS {}: {}", cid, e.getMessage());
            throw new IPFSException("Failed to get JSON from IPFS", e);
        }
    }

    /**
     * Lấy JSON từ IPFS dưới dạng Map
     * 
     * @param cid IPFS CID
     * @return Map chứa data
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getJsonAsMap(String cid) {
        return getJson(cid, Map.class);
    }

    /**
     * Lấy file bytes từ IPFS
     * 
     * @param cid IPFS CID
     * @return Byte array của file
     */
    public byte[] getFile(String cid) {
        try {
            log.debug("Getting file from IPFS: {}", cid);

            String url = getGatewayUrl(cid);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IPFSException("Failed to get file from IPFS: " + response.code());
                }

                return response.body().bytes();
            }
        } catch (IPFSException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting file from IPFS {}: {}", cid, e.getMessage());
            throw new IPFSException("Failed to get file from IPFS", e);
        }
    }

    // =====================================================
    // UTILITY METHODS
    // =====================================================

    /**
     * Tạo gateway URL từ CID
     * 
     * @param cid IPFS CID
     * @return Full URL để truy cập file
     */
    public String getGatewayUrl(String cid) {
        return pinataConfig.getGatewayUrl() + cid;
    }

    /**
     * Kiểm tra CID có tồn tại không
     * 
     * @param cid IPFS CID
     * @return true nếu tồn tại
     */
    public boolean verifyCid(String cid) {
        try {
            String url = getGatewayUrl(cid);

            Request request = new Request.Builder()
                    .url(url)
                    .head() // Chỉ lấy header, không lấy body
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (Exception e) {
            log.error("Error verifying CID {}: {}", cid, e.getMessage());
            return false;
        }
    }

    /**
     * Upload image từ base64 string
     * 
     * @param base64 Base64 encoded image
     * @param name   Tên file
     * @return IPFS CID
     */
    public String uploadBase64Image(String base64, String name) {
        try {
            // Remove data URL prefix if present
            String base64Data = base64;
            String mimeType = "image/png"; // default

            if (base64.contains(",")) {
                String[] parts = base64.split(",");
                // Parse mime type from "data:image/png;base64"
                if (parts[0].contains(":") && parts[0].contains(";")) {
                    mimeType = parts[0].split(":")[1].split(";")[0];
                }
                base64Data = parts[1];
            }

            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Data);
            return uploadBytes(imageBytes, name, mimeType);
        } catch (Exception e) {
            log.error("Error uploading base64 image: {}", e.getMessage());
            throw new IPFSException("Failed to upload base64 image", e);
        }
    }

    /**
     * Lưu record upload vào database
     */
    private void saveUploadRecord(String cid, String contentType, String name, Long fileSize, String mimeType) {
        try {
            IPFSUpload upload = IPFSUpload.builder()
                    .cid(cid)
                    .contentType(contentType)
                    .referenceId(name)
                    .fileSize(fileSize)
                    .mimeType(mimeType)
                    .build();

            ipfsUploadRepository.save(upload);
            log.debug("Saved IPFS upload record: {}", cid);
        } catch (Exception e) {
            // Không throw exception, chỉ log warning
            log.warn("Failed to save IPFS upload record for {}: {}", cid, e.getMessage());
        }
    }
}
