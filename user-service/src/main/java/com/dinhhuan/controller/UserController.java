package com.dinhhuan.controller;

import com.dinhhuan.dto.request.UpdateWalletRequest;
import com.dinhhuan.dto.request.UserCreationRequest;
import com.dinhhuan.dto.response.UserInfoResponse;
import com.dinhhuan.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @GetMapping(path = "/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestHeader("X-User-Id") String userId) {
        var response = userService.getUserInfo(Long.parseLong(userId));
        return ResponseEntity.ok(response);
    }
    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody UserCreationRequest user){
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Update wallet address for user
     * Called from frontend after user connects MetaMask
     *
     * PATCH /user/wallet
     * Header: X-User-Id
     * Body: { "walletAddress": "0x..." }
     */
    @PatchMapping("/wallet")
    public ResponseEntity<?> updateWalletAddress(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UpdateWalletRequest request) {
        userService.updateWalletAddress(Long.parseLong(userId), request.getWalletAddress());
        return ResponseEntity.ok().build();
    }
}
