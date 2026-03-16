package com.jakob.secure_case_management_api.controller;


import com.jakob.secure_case_management_api.dto.*;
import com.jakob.secure_case_management_api.model.RefreshToken;
import com.jakob.secure_case_management_api.security.JwtService;
import com.jakob.secure_case_management_api.service.AuthService;
import com.jakob.secure_case_management_api.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        System.out.println("LOGIN ENDPOINT HIT");

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {

        refreshTokenService.revokeToken(request.getRefreshToken());

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshRequest request) {

        RefreshToken token =
                refreshTokenService.verifyExpiration(request.getRefreshToken());

        String accessToken =
                jwtService.generateToken(token.getUser());

        return ResponseEntity.ok(
                new AuthResponse(accessToken, request.getRefreshToken())
        );
    }
}