package com.jakob.secure_case_management_api.service;

import com.jakob.secure_case_management_api.dto.CaseCreateRequest;
import com.jakob.secure_case_management_api.dto.CaseResponse;
import com.jakob.secure_case_management_api.dto.CaseUpdateRequest;
import com.jakob.secure_case_management_api.exception.ResourceNotFoundException;
import com.jakob.secure_case_management_api.model.*;
import com.jakob.secure_case_management_api.repository.CaseAuditLogRepository;
import com.jakob.secure_case_management_api.repository.CaseRepository;
import com.jakob.secure_case_management_api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CaseService {

    private final CaseRepository caseRepository;
    private final UserRepository userRepository;
    private final CaseAuditLogRepository auditLogRepository;

    public CaseService(CaseRepository caseRepository,
                       CaseAuditLogRepository auditLogRepository,
                       UserRepository userRepository) {
        this.caseRepository = caseRepository;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void changeStatus(Long caseId, CaseStatus newStatus, User currentUser) {

        Case caseEntity = caseRepository.findByIdAndDeletedFalse(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        if (caseEntity.getStatus() == CaseStatus.CLOSED) {
            throw new IllegalStateException("Closed cases cannot be modified");
        }

        boolean isAssignedUser =
                caseEntity.getAssignedTo() != null &&
                        caseEntity.getAssignedTo().getId().equals(currentUser.getId());

        if (!isAssignedUser && !currentUser.hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Not allowed to change status");
        }

        CaseStatus oldStatus = caseEntity.getStatus();

        validateTransition(oldStatus, newStatus);

        caseEntity.setStatus(newStatus);
        caseRepository.save(caseEntity);

        auditLogRepository.save(
                CaseAuditLog.builder()
                        .caseEntity(caseEntity)
                        .oldStatus(oldStatus)
                        .newStatus(newStatus)
                        .changedBy(currentUser)
                        .build()
        );
    }

    private void validateTransition(CaseStatus oldStatus, CaseStatus newStatus) {

        if (oldStatus == newStatus) {
            throw new IllegalStateException("Case is already in status: " + newStatus);
        }

        switch (oldStatus) {

            case OPEN:
                if (newStatus == CaseStatus.IN_PROGRESS ||
                        newStatus == CaseStatus.CLOSED) {
                    return;
                }
                break;

            case IN_PROGRESS:
                if (newStatus == CaseStatus.CLOSED) {
                    return;
                }
                break;

            default:
                break;
        }

        throw new IllegalStateException(
                "Invalid status transition from " + oldStatus + " to " + newStatus
        );
    }

    @Transactional(readOnly = true)
    public Page<CaseResponse> getCases(User currentUser, Pageable pageable) {

        Page<Case> page;

        if (currentUser.hasRole("ROLE_ADMIN")) {
            page = caseRepository.findByDeletedFalse(pageable);
        } else {
            page = caseRepository.findByAssignedTo_IdAndDeletedFalse(
                    currentUser.getId(), pageable);
        }

        return page.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public CaseResponse getCaseById(Long id, User currentUser) {

        Case caseEntity = caseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        if (!caseEntity.getCreatedBy().getId().equals(currentUser.getId())
                && (caseEntity.getAssignedTo() == null ||
                !caseEntity.getAssignedTo().getId().equals(currentUser.getId()))
                && !currentUser.hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Not allowed to view case");
        }

        return mapToResponse(caseEntity);
    }

    @Transactional
    public CaseResponse createCase(CaseCreateRequest request, User currentUser) {

        User assignedUser = null;

        if (request.getAssignedToUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        Case caseEntity = new Case();
        caseEntity.setTitle(request.getTitle());
        caseEntity.setDescription(request.getDescription());
        caseEntity.setPriority(request.getPriority());
        caseEntity.setStatus(CaseStatus.OPEN);
        caseEntity.setCreatedBy(currentUser);
        caseEntity.setAssignedTo(assignedUser);

        Case saved = caseRepository.save(caseEntity);

        return mapToResponse(saved);
    }

    private CaseResponse mapToResponse(Case caseEntity) {

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

    @Transactional
    public void deleteCase(Long id, User currentUser) {

        Case caseEntity = caseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        if (!caseEntity.getCreatedBy().getId().equals(currentUser.getId())
                && !currentUser.hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Not allowed to delete case");
        }

        caseEntity.setDeleted(true);
    }

    @Transactional
    public CaseResponse updateCase(Long id,
                                   CaseUpdateRequest request,
                                   User currentUser) {

        Case caseEntity = caseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        if (!caseEntity.getCreatedBy().getId().equals(currentUser.getId())
                && !currentUser.hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Not allowed to update case");
        }

        if (caseEntity.getStatus() == CaseStatus.CLOSED) {
            throw new IllegalStateException("Closed cases cannot be edited");
        }

        caseEntity.setTitle(request.getTitle());
        caseEntity.setDescription(request.getDescription());

        if (request.getPriority() != null) {
            caseEntity.setPriority(request.getPriority());
        }

        return mapToResponse(caseEntity);
    }
}