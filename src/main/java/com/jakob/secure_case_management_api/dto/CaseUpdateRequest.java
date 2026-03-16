package com.jakob.secure_case_management_api.dto;

import com.jakob.secure_case_management_api.model.CasePriority;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseUpdateRequest {

    @NotBlank
    private String title;

    private String description;

    private CasePriority priority;
}