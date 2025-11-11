package com.dinhhuan.controller;

import com.dinhhuan.commons.regular.Data;
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
}
