package com.dinhhuan.loyaltyservice.exception;

/**
 * Exception xảy ra khi tương tác với IPFS/Pinata
 * Ví dụ: Upload failed, File not found, API error...
 */
public class IPFSException extends RuntimeException {
    
    public IPFSException(String message) {
        super(message);
    }
    
    public IPFSException(String message, Throwable cause) {
        super(message, cause);
    }
}