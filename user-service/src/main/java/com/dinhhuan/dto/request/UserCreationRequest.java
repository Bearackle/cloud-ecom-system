package com.dinhhuan.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreationRequest {
    private String fullName;
    private String email;
    private String location;
    private String phoneNumber;
    private String gender;
    private LocalDate birthday;
}
