package com.dinhhuan.service;

import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.model.User;

public interface UserService {
    //internal callback
    Long createUser(UserRegistrationDto user);
    void updateUser(User user);
    User getUserById(Long id);
    void deleteUserById(Long id);
}
