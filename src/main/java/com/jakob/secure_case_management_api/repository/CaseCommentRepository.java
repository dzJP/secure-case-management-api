package com.jakob.secure_case_management_api.repository;

import com.jakob.secure_case_management_api.model.CaseComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseCommentRepository extends JpaRepository<CaseComment, Long> {

    List<CaseComment> findByCaseEntityId(Long caseId);

    List<CaseComment> findByCaseEntityIdOrderByCreatedAtAsc(Long caseId);
}
