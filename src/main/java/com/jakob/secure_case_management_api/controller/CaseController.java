package com.jakob.secure_case_management_api.controller;

import com.jakob.secure_case_management_api.dto.CaseCreateRequest;
import com.jakob.secure_case_management_api.dto.CaseResponse;
import com.jakob.secure_case_management_api.dto.CaseUpdateRequest;
import com.jakob.secure_case_management_api.model.User;
import com.jakob.secure_case_management_api.service.CaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    @PostMapping
    public ResponseEntity<CaseResponse> create(
            @Valid @RequestBody CaseCreateRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(caseService.createCase(request, currentUser));
    }

    @GetMapping
    public ResponseEntity<Page<CaseResponse>> getAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
                caseService.getCases(currentUser, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaseResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
                caseService.getCaseById(id, currentUser)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CaseUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
                caseService.updateCase(id, request, currentUser)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        caseService.deleteCase(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
