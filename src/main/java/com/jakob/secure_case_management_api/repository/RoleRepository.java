package com.jakob.secure_case_management_api.repository;

import com.jakob.secure_case_management_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}