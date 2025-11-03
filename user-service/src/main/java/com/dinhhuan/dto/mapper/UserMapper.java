package com.dinhhuan.dto.mapper;

import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.dto.request.UserCreationRequest;
import com.dinhhuan.dto.response.UserInfoResponse;
import com.dinhhuan.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserInfoResponse toUserInfoDto(User user);
    User toUser(UserCreationRequest userInfoResponse);
    UserCreationRequest toUserCreationRequest(UserRegistrationDto user);
}
