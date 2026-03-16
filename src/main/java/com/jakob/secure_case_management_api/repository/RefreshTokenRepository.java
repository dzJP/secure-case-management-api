package com.jakob.secure_case_management_api.repository;

import com.jakob.secure_case_management_api.model.RefreshToken;
import com.jakob.secure_case_management_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}