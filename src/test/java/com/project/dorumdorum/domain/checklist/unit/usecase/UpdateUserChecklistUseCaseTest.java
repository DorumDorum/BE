package com.project.dorumdorum.domain.checklist.unit.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.mapper.UserChecklistMapper;
import com.project.dorumdorum.domain.checklist.application.usecase.UpdateUserChecklistUseCase;
import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.checklist.domain.service.UserChecklistService;
import com.project.dorumdorum.domain.checklist.unit.ChecklistFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateUserChecklistUseCase Unit Tests")
class UpdateUserChecklistUseCaseTest {

    @Mock private UserChecklistService userChecklistService;
    @Mock private UserChecklistMapper userChecklistMapper;
    @InjectMocks private UpdateUserChecklistUseCase useCase;

    @Test
    void execute_UpdatesAndSavesChecklist() {
        UpdateUserChecklistRequest request = ChecklistFixtures.updateUserChecklistRequest();
        UserChecklist checklist = UserChecklist.builder().userNo("u1").build();
        when(userChecklistService.findByUserNo("u1")).thenReturn(checklist);

        useCase.execute("u1", request);

        verify(userChecklistMapper).updateUserChecklist(request, checklist);
        verify(userChecklistService).save(checklist);
    }
}
