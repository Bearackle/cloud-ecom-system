package com.dinhhuan.auth.controller;

import com.dinhhuan.auth.dto.UserAuthRequest;
import com.dinhhuan.auth.dto.UserCredentials;
import com.dinhhuan.auth.service.AuthenticationService;
import com.dinhhuan.commons.auth.UserLoginRequest;
import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.commons.regular.Data;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<Data<Map<String,String>>> login(@RequestBody UserLoginRequest userLogin){
        Map<String, String> result = new HashMap<>();
        String token = authenticationService.login(userLogin);
        if(token == null){
            return ResponseEntity.notFound().build();
        }
        result.put("token", token);
        return ResponseEntity.ok(Data.<Map<String,String>>builder().data(result).build());
    }
    @PostMapping("/register")
    public ResponseEntity<Data<?>> register(@RequestBody UserRegistrationDto user){
        Map<String, String> result = new HashMap<>();
        long userId = authenticationService.register(user);
        if(userId == 0){
            return ResponseEntity.badRequest().build();
        }
        result.put("userId", String.valueOf(userId));
        return ResponseEntity.ok(Data.<Map<String,String>>builder().data(result).build());
    }
    @PostMapping("/authenticate")
    public ResponseEntity<Data<Map<String, Boolean>>> authenticate(@RequestBody UserAuthRequest userAuth){
        boolean isAuthenticated = authenticationService.authenticate(userAuth);
        Map<String, Boolean> result = new HashMap<>();
        result.put("isAuthenticated", isAuthenticated);
        return ResponseEntity.ok(
                Data.<Map<String, Boolean>>builder().data(result)
                        .build());
    }
    @PostMapping("/change-password")
    public ResponseEntity<Data<Map<String,Boolean>>> changePassword(@RequestBody UserCredentials userCredentials)
    {
        Map<String, Boolean> result = new HashMap<>();
        Long userId = authenticationService.updatePassword(userCredentials);
        if(userId == null){
            return ResponseEntity.badRequest().build();
        }
        Boolean isUpdated = userId != 0;
        result.put("isUpdated", isUpdated);
        return ResponseEntity.ok(
                Data.<Map<String, Boolean>>builder()
                        .data(result)
                        .build()
        );
    }
}
