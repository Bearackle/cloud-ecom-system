package com.dinhhuan.dto;

import com.dinhhuan.model.Address;

import java.time.LocalDate;
import java.util.List;

public class UserInfoDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private LocalDate birthday;
    private List<Address> address;
}
