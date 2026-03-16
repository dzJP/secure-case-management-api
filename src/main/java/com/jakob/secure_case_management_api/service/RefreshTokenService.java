package com.jakob.secure_case_management_api.service;

import com.jakob.secure_case_management_api.model.RefreshToken;
import com.jakob.secure_case_management_api.model.User;
import com.jakob.secure_case_management_api.repository.RefreshTokenRepository;
import com.jakob.secure_case_management_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpirationMs;

    public RefreshTokenService(RefreshTokenRepository repository,
                               UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000))
                .revoked(false)
                .build();

        return repository.save(token);
    }

    public RefreshToken verifyExpiration(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isRevoked() ||
                refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            repository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired or revoked");
        }

        return refreshToken;
    }

    public void revokeToken(String token) {
        repository.findByToken(token)
                .ifPresent(t -> {
                    t.setRevoked(true);
                    repository.save(t);
                });
    }
}