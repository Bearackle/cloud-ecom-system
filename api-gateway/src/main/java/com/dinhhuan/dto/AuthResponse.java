package com.dinhhuan.dto;


public class AuthResponse {
    private boolean isAuthenticated;
    private Long userId;
    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }
    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
