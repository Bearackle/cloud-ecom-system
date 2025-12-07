package com.dinhhuan.loyaltyservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework. web.bind.annotation. ExceptionHandler;
import org.springframework. web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util. HashMap;
import java.util.Map;

/**
 * Global Exception Handler - Xử lý tất cả exception trong application
 * Trả về response format thống nhất cho client
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Xử lý lỗi Blockchain
     */
    @ExceptionHandler(BlockchainException.class)
    public ResponseEntity<Map<String, Object>> handleBlockchainException(BlockchainException ex) {
        log.error("Blockchain error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "BLOCKCHAIN_ERROR", ex.getMessage());
    }
    
    /**
     * Xử lý lỗi IPFS
     */
    @ExceptionHandler(IPFSException.class)
    public ResponseEntity<Map<String, Object>> handleIPFSException(IPFSException ex) {
        log.error("IPFS error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "IPFS_ERROR", ex.getMessage());
    }
    
    /**
     * Xử lý lỗi nghiệp vụ
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        log.warn("Business error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus. BAD_REQUEST, "BUSINESS_ERROR", ex.getMessage());
    }
    
    /**
     * Xử lý các lỗi khác
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log. error("Unexpected error: {}", ex. getMessage(), ex);
        return buildErrorResponse(HttpStatus. INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", 
            "An unexpected error occurred");
    }
    
    /**
     * Helper method để build error response
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status, String errorCode, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("timestamp", LocalDateTime.now(). toString());
        body.put("status", status.value());
        body.put("errorCode", errorCode);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
} 
  
