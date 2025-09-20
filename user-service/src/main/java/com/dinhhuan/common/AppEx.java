package com.dinhhuan.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppEx extends RuntimeException {
    private final HttpStatus status;
    public AppEx(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
