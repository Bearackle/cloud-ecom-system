package com.dinhhuan.enums;

public enum OrderStatus {
    PENDING,
    PAID,
    CANCELED;
    public static OrderStatus fromCode(int code) {
        for (OrderStatus s : values()) {
            if (s.ordinal() == code) return s;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}