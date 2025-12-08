package com.dinhhuan.service.impl;

import com.dinhhuan.common.AppEx;
import com.dinhhuan.dto.mapper.UserMapper;
import com.dinhhuan.dto.request.UserCreationRequest;
import com.dinhhuan.dto.response.UserInfoResponse;
import com.dinhhuan.model.User;
import com.dinhhuan.repository.UserRepository;
import com.dinhhuan.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
        public UserInfoResponse getUserInfo(Long userId) {
        log.info("getUserInfo {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new AppEx("User not found", HttpStatus.NOT_FOUND));
        return UserInfoResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .language(user.getLanguage())
                .walletAddress(user.getWalletAddress())
                .build();
    }
    @Override
    public void createUser(UserCreationRequest user) {
        User newUser = UserMapper.INSTANCE.toUser(user);
        userRepository.save(newUser);
    }

    @Override
    public void updateWalletAddress(Long userId, String walletAddress) {
        log.info("Updating wallet address for user {}: {}", userId, walletAddress);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppEx("User not found", HttpStatus.NOT_FOUND));

        // Validate wallet address format (0x followed by 40 hex characters)
        if (walletAddress != null && !walletAddress.matches("^0x[a-fA-F0-9]{40}$")) {
            throw new AppEx("Invalid Ethereum wallet address format", HttpStatus.BAD_REQUEST);
        }

        user.setWalletAddress(walletAddress);
        userRepository.save(user);
        log.info("Wallet address updated successfully for user {}", userId);
    }
}
