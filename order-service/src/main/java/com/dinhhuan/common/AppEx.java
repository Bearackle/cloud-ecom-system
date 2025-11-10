package com.dinhhuan.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppEx extends RuntimeException {
    private final HttpStatus httpStatus;
    public AppEx(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
