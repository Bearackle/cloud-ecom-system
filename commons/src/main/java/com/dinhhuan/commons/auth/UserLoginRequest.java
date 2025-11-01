package com.dinhhuan.commons.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLoginRequest {
    private String email;
    private String password;
}
