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
    public Long createOrUpdate(UserRegistrationDto user) {
        User targetUser = userRepository.findById(user.getId()).orElse(null);
        if(targetUser == null){
            targetUser = new User();
            targetUser.setId(uid.getUID());
        }
        targetUser.setEmail(user.getEmail());
        targetUser.setPhone(user.getPhoneNumber());
        userRepository.save(targetUser);
        return targetUser.getId();
    }
}
