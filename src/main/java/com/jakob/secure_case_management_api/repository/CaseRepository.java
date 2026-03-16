package com.jakob.secure_case_management_api.repository;

import com.jakob.secure_case_management_api.model.Case;
import com.jakob.secure_case_management_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaseRepository extends JpaRepository<Case, Long> {

    Page<Case> findByDeletedFalse(Pageable pageable);

    Page<Case> findByAssignedTo_IdAndDeletedFalse(Long userId, Pageable pageable);

    Page<Case> findByCreatedBy_IdAndDeletedFalse(Long userId, Pageable pageable);

    Optional<Case> findByIdAndDeletedFalse(Long id);
}