package com.dinhhuan.service;

import com.dinhhuan.dto.UserFullInfo;
import com.dinhhuan.dto.UserInfoDto;


public interface UserService {
    UserInfoDto getUserInfo(Long userId);
    void createUser(UserFullInfo user);
}
