package com.jakob.secure_case_management_api;

import com.jakob.secure_case_management_api.exception.ResourceNotFoundException;
import com.jakob.secure_case_management_api.model.Case;
import com.jakob.secure_case_management_api.model.CaseStatus;
import com.jakob.secure_case_management_api.model.User;
import com.jakob.secure_case_management_api.repository.CaseAuditLogRepository;
import com.jakob.secure_case_management_api.repository.CaseRepository;
import com.jakob.secure_case_management_api.service.CaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private CaseAuditLogRepository auditLogRepository;

    @InjectMocks
    private CaseService caseService;

    @Test
    void shouldThrowWhenInvalidTransition() {

        Case caseEntity = new Case();
        caseEntity.setId(1L);
        caseEntity.setStatus(CaseStatus.CLOSED);

        User user = new User();
        user.setId(1L);

        caseEntity.setAssignedTo(user);

        when(caseRepository.findByIdAndDeletedFalse(anyLong()))
                .thenReturn(Optional.of(caseEntity));

        assertThrows(IllegalStateException.class, () ->
                caseService.changeStatus(1L, CaseStatus.OPEN, user)
        );
    }

    @Test
    void shouldThrowWhenCaseNotFound() {

        User user = new User();
        user.setId(1L);

        when(caseRepository.findByIdAndDeletedFalse(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                caseService.changeStatus(1L, CaseStatus.IN_PROGRESS, user)
        );
    }

    @Test
    void shouldThrowAccessDeniedWhenUserNotAssigned() {

        Case caseEntity = new Case();
        caseEntity.setStatus(CaseStatus.OPEN);

        User assignedUser = new User();
        assignedUser.setId(2L);

        caseEntity.setAssignedTo(assignedUser);

        User otherUser = new User();
        otherUser.setId(3L);

        when(caseRepository.findByIdAndDeletedFalse(anyLong()))
                .thenReturn(Optional.of(caseEntity));

        assertThrows(AccessDeniedException.class, () ->
                caseService.changeStatus(1L, CaseStatus.IN_PROGRESS, otherUser)
        );
    }

    @Test
    void shouldChangeStatusSuccessfully() {

        Case caseEntity = new Case();
        caseEntity.setStatus(CaseStatus.OPEN);

        User user = new User();
        user.setId(1L);

        caseEntity.setAssignedTo(user);

        when(caseRepository.findByIdAndDeletedFalse(anyLong()))
                .thenReturn(Optional.of(caseEntity));

        caseService.changeStatus(1L, CaseStatus.IN_PROGRESS, user);

        assertEquals(CaseStatus.IN_PROGRESS, caseEntity.getStatus());

        verify(caseRepository).save(caseEntity);
        verify(auditLogRepository).save(any());
    }
}