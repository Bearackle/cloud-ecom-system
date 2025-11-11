package com.dinhhuan.dto.response;

import com.dinhhuan.model.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse{
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String language;
}
