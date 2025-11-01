package com.dinhhuan.auth.service;

public interface AuthorizationService {
    boolean authorize(String username, String path);
}
