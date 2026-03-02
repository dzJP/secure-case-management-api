package com.jakob.secure_case_management_api.security;

import com.jakob.secure_case_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities(
                                user.getRoles()
                                        .stream()
                                        .map(role -> role.getName())
                                        .toArray(String[]::new)
                        )
                        .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }
}