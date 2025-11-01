package com.dinhhuan.auth.service.impl;

import com.dinhhuan.auth.model.User;
import com.dinhhuan.auth.model.UserIdDetails;
import com.dinhhuan.auth.repository.UserRepository;
import com.dinhhuan.auth.service.UserDetailsSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsSevice {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserById(long userId) {
       Optional<User> user = userRepository.findById(userId);
       if(user.isPresent()) {
           UserIdDetails userIdDetails =
                   UserIdDetails.builder()
                           .userId(user.get().getId())
                           .email(user.get().getEmail())
                           .password(user.get().getPassword())
                           .authorities(List.of())
                           .build();
           return userIdDetails;
       }
       throw new UsernameNotFoundException("User not found");
    }
}
