package com.project.dorumdorum.domain.checklist.unit.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.mapper.UserChecklistMapper;
import com.project.dorumdorum.domain.checklist.application.usecase.CreateUserChecklistUseCase;
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
@DisplayName("CreateUserChecklistUseCase Unit Tests")
class CreateUserChecklistUseCaseTest {

    @Mock private UserChecklistService userChecklistService;
    @Mock private UserChecklistMapper userChecklistMapper;
    @InjectMocks private CreateUserChecklistUseCase useCase;

    @Test
    void execute_MapsAndSavesChecklist() {
        CreateUserChecklistRequest request = ChecklistFixtures.createUserChecklistRequest();
        UserChecklist entity = UserChecklist.builder().userNo("u1").build();
        when(userChecklistMapper.toUserChecklist("u1", request)).thenReturn(entity);

        useCase.execute("u1", request);

        verify(userChecklistMapper).toUserChecklist("u1", request);
        verify(userChecklistService).save(entity);
    }
}
