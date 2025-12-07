package com.dinhhuan.loyaltyservice.exception;

/**
 * Exception cho các lỗi nghiệp vụ
 * Ví dụ: Customer already registered, Insufficient balance, Invalid reward...
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}