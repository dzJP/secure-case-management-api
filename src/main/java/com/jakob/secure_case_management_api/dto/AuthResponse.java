package com.jakob.secure_case_management_api.dto;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;

    public AuthResponse() {}

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}