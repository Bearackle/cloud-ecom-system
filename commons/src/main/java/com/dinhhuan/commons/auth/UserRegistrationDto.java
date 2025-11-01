package com.dinhhuan.commons.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
}
