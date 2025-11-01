package com.dinhhuan.auth.service;

import com.dinhhuan.auth.dto.UserAuthRequest;
import com.dinhhuan.auth.dto.UserCredentials;
import com.dinhhuan.commons.auth.UserLoginRequest;
import com.dinhhuan.commons.auth.UserRegistrationDto;

public interface AuthenticationService {
    long register(UserRegistrationDto userRegistration);
    String login(UserLoginRequest userLogin);
    boolean authenticate(UserAuthRequest userAuth);
    Long updatePassword(UserCredentials userAuth);
}
