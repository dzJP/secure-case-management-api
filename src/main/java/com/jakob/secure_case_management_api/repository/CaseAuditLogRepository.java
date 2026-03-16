package com.jakob.secure_case_management_api.repository;

import com.jakob.secure_case_management_api.model.CaseAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseAuditLogRepository extends JpaRepository<CaseAuditLog, Long> {
}