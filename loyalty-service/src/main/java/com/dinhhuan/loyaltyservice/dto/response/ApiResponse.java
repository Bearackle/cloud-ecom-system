package com.dinhhuan.loyaltyservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic API Response wrapper
 * 
 * Tất cả response từ API đều được wrap trong class này để:
 * - Có format thống nhất
 * - Dễ dàng xử lý ở frontend
 * - Chứa thông tin success/error
 * 
 * @param <T> Kiểu dữ liệu của data trả về
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Không serialize các field null
public class ApiResponse<T> {

    /**
     * Trạng thái request: true = thành công, false = lỗi
     */
    private boolean success;

    /**
     * Thông báo cho user (success message hoặc error message)
     */
    private String message;

    /**
     * Dữ liệu trả về (chỉ có khi success = true)
     */
    private T data;

    /**
     * Timestamp của response
     */
    @Builder.Default
    private String timestamp = LocalDateTime.now().toString();

    /**
     * Tạo response thành công với data
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    /**
     * Tạo response thành công với data và message
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Tạo response thành công chỉ có message (không có data)
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

    /**
     * Tạo response lỗi
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
