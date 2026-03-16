package com.jakob.secure_case_management_api.dto;

import com.jakob.secure_case_management_api.model.CasePriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseCreateRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private CasePriority priority;

    private Long assignedToUserId; // optional
}