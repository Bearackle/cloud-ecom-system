package com.dinhhuan.controller;

import com.dinhhuan.common.Data;
import com.dinhhuan.dto.request.UserCreationRequest;
import com.dinhhuan.dto.response.UserInfoResponse;
import com.dinhhuan.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    @GetMapping(path = "/info/{id}")
    public ResponseEntity<Data<UserInfoResponse>> getUserInfo(@PathVariable("id") Long userId) {
        Data<UserInfoResponse> response = Data.<UserInfoResponse>builder().code(HttpStatus.OK.value()).message("success").data(userService.getUserInfo(userId)).build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody UserCreationRequest user){
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
