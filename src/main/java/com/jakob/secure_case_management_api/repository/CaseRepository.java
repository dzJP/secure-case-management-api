package com.jakob.secure_case_management_api.repository;

import com.jakob.secure_case_management_api.model.CaseEntity;
import com.jakob.secure_case_management_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseRepository extends JpaRepository<CaseEntity, Long> {

    List<CaseEntity> findByCreatedBy(User user);

    List<CaseEntity> findByAssignedTo(User user);
}