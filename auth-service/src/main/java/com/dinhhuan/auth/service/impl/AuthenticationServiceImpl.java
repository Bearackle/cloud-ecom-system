package com.dinhhuan.auth.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.auth.dto.UserAuthRequest;
import com.dinhhuan.auth.dto.UserCredentials;
import com.dinhhuan.auth.model.User;
import com.dinhhuan.auth.model.UserIdDetails;
import com.dinhhuan.auth.producer.UserCreationProducer;
import com.dinhhuan.auth.repository.UserRepository;
import com.dinhhuan.auth.service.AuthenticationService;
import com.dinhhuan.auth.util.JwtUtils;
import com.dinhhuan.commons.auth.UserLoginRequest;
import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.commons.uid.DefaultUid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCreationProducer producer;
    private final JwtUtils tokenUtils;
    private final DefaultUidGenerator uid;
    @Override
    public long register(UserRegistrationDto userRegistration) {
        User user = new User();
        user.setEmail(userRegistration.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.setPhoneNumber(userRegistration.getPhoneNumber());
        user.setId(uid.getUID());
        userRepository.save(user);
        userRegistration.setId(user.getId());
        producer.sendMessage(userRegistration);
        return user.getId();
    }
    @Override
    public String login(UserLoginRequest userLogin) {
        Optional<User> user = userRepository.findByEmail(userLogin.getEmail());
        if (!user.isPresent()) {
            return null;
        }
        if(!passwordEncoder.matches(userLogin.getPassword(), user.get().getPassword())) {
            return null;
        }
        UserIdDetails userDetails = new
                UserIdDetails(user.get().getId(), user.get().getEmail(), user.get().getPassword(), List.of());
        return tokenUtils.generateToken(userDetails);
    }

    @Override
    public boolean authenticate(UserAuthRequest userAuth) {
        var user = userRepository.findById(userAuth.getUserId());
        if(!user.isPresent()){
            return false;
        }
        return passwordEncoder.matches(userAuth.getPassword(), user.get().getPassword());
    }

    @Override
    public Long updatePassword(UserCredentials userAuth) {
        return 0L;
    }
}
