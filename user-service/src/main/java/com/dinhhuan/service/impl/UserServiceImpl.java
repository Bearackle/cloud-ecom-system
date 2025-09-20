package com.dinhhuan.service.impl;

import com.dinhhuan.common.AppEx;
import com.dinhhuan.dto.UserFullInfo;
import com.dinhhuan.dto.UserInfoDto;
import com.dinhhuan.dto.mapper.UserMapper;
import com.dinhhuan.model.User;
import com.dinhhuan.repository.UserRepository;
import com.dinhhuan.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserInfoDto getUserInfo(Long userId) {
        System.out.println("GET here ---------------");
        User user = userRepository.findById(userId).orElseThrow(() -> new AppEx("User not found", HttpStatus.NOT_FOUND));
        return UserMapper.INSTANCE.toUserInfoDto(user);
    }
    @Override
    public void createUser(UserFullInfo user) {
        User newUser = UserMapper.INSTANCE.toUser(user);
        userRepository.save(newUser);
    }
}
