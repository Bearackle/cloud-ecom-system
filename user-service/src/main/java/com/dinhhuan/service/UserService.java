package com.dinhhuan.service;

import com.dinhhuan.dto.request.UserCreationRequest;
import com.dinhhuan.dto.response.UserInfoResponse;



public interface UserService {
    UserInfoResponse getUserInfo(Long userId);
    void createUser(UserCreationRequest user);
}
