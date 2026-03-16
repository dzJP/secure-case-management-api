package com.jakob.secure_case_management_api.dto;

import com.jakob.secure_case_management_api.model.CasePriority;
import com.jakob.secure_case_management_api.model.CaseStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CaseResponse {

    private Long id;
    private String title;
    private String description;
    private CaseStatus status;
    private CasePriority priority;

    private Long createdById;
    private String createdByEmail;

    private Long assignedToId;
    private String assignedToEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}