package com.project.dorumdorum.domain.checklist.unit.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.response.UserChecklistResponse;
import com.project.dorumdorum.domain.checklist.application.mapper.UserChecklistMapper;
import com.project.dorumdorum.domain.checklist.application.usecase.LoadUserChecklistUseCase;
import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.checklist.domain.service.UserChecklistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadUserChecklistUseCase Unit Tests")
class LoadUserChecklistUseCaseTest {

    @Mock private UserChecklistService userChecklistService;
    @Mock private UserChecklistMapper userChecklistMapper;
    @InjectMocks private LoadUserChecklistUseCase useCase;

    @Test
    void execute_LoadsAndMapsChecklist() {
        UserChecklist checklist = UserChecklist.builder().userNo("u1").build();
        UserChecklistResponse response = UserChecklistResponse.builder().bedtime("23:00").build();
        when(userChecklistService.findByUserNo("u1")).thenReturn(checklist);
        when(userChecklistMapper.toResponse(checklist)).thenReturn(response);

        UserChecklistResponse result = useCase.execute("u1");

        assertThat(result).isEqualTo(response);
        verify(userChecklistService).findByUserNo("u1");
        verify(userChecklistMapper).toResponse(checklist);
    }
}
