package com.dinhhuan.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCredentials {
    private String email;
    private String password;
}
