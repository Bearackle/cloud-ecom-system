package com.dinhhuan.auth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserCredentials {
    private String email;
    private String password;
}
