package com.dinhhuan.service;

import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.model.User;

public interface UserService {
    //internal callback
    Long createOrUpdate(UserRegistrationDto user);
}
