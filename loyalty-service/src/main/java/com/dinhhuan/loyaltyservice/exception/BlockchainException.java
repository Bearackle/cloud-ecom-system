package com.dinhhuan.loyaltyservice.exception;

/**
 * Exception xảy ra khi tương tác với Blockchain
 * Ví dụ: Transaction failed, Contract call error, Invalid address... 
 */
public class BlockchainException extends RuntimeException {
    
    public BlockchainException(String message) {
        super(message);
    }

    public BlockchainException(String message, Throwable cause) {
        super(message, cause);
    }
}