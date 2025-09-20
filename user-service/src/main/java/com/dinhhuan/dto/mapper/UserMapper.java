package com.dinhhuan.dto.mapper;

import com.dinhhuan.dto.UserFullInfo;
import com.dinhhuan.dto.UserInfoDto;
import com.dinhhuan.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserInfoDto toUserInfoDto(User user);
    User toUser(UserFullInfo userFullInfo);
}
