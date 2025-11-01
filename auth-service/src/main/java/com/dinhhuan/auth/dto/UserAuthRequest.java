package com.dinhhuan.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthRequest {
    private Long userId;
    private String password;
}
