package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.commons.uid.DefaultUid;
import com.dinhhuan.model.User;
import com.dinhhuan.repository.UserRepository;
import com.dinhhuan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final DefaultUidGenerator uid;
    private final UserRepository userRepository;
    @Override
    public Long createUser(UserRegistrationDto user) {
        User newUser = new User();
        newUser.setId(uid.getUID());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhoneNumber());
        userRepository.save(newUser);
        return newUser.getId();
    }
    @Override
    public void updateUser(User user) {
        User updateUser = userRepository.findById(user.getId()).orElse(null);
        if(updateUser == null){
            return;
        }
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        userRepository.save(updateUser);
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
