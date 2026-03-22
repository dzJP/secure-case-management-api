package com.jakob.secure_case_management_api.mapper;

import com.jakob.secure_case_management_api.dto.CaseResponse;
import com.jakob.secure_case_management_api.model.Case;

public class CaseMapper {

    private CaseMapper() {}

    public static CaseResponse toResponse(Case caseEntity) {

        return CaseResponse.builder()
                .id(caseEntity.getId())
                .title(caseEntity.getTitle())
                .description(caseEntity.getDescription())
                .status(caseEntity.getStatus())
                .priority(caseEntity.getPriority())
                .createdById(caseEntity.getCreatedBy().getId())
                .createdByEmail(caseEntity.getCreatedBy().getEmail())
                .assignedToId(
                        caseEntity.getAssignedTo() != null
                                ? caseEntity.getAssignedTo().getId()
                                : null
                )
                .assignedToEmail(
                        caseEntity.getAssignedTo() != null
                                ? caseEntity.getAssignedTo().getEmail()
                                : null
                )
                .createdAt(caseEntity.getCreatedAt())
                .updatedAt(caseEntity.getUpdatedAt())
                .build();
    }
}