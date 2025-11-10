package com.dinhhuan.common;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppEx.class)
    public ResponseEntity<Data<Object>> handleAppEx(AppEx ex) {
        var body = Data.builder()
                .code(ex.getHttpStatus().value())
                .message(ex.getMessage())
                .data(null)
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Data<Object>> handleAll(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        var body = Data.builder()
                .code(status.value())
                .message("Unexpected error: " + ex.getMessage())
                .data(null)
                .build();

        return ResponseEntity.status(status).body(body);
    }
}