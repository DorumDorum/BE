package com.project.dorumdorum.domain.checklist.unit.ui;

import com.project.dorumdorum.domain.checklist.application.dto.response.UserChecklistResponse;
import com.project.dorumdorum.domain.checklist.application.usecase.LoadUserChecklistUseCase;
import com.project.dorumdorum.domain.checklist.ui.LoadUserChecklistController;
import com.project.dorumdorum.global.common.BaseResponse;
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
@DisplayName("LoadUserChecklistController Unit Tests")
class LoadUserChecklistControllerTest {

    @Mock private LoadUserChecklistUseCase useCase;
    @InjectMocks private LoadUserChecklistController controller;

    @Test
    void loadMyChecklist_ReturnsResponse() {
        UserChecklistResponse payload = UserChecklistResponse.builder().bedtime("23:00").build();
        when(useCase.execute("u1")).thenReturn(payload);

        BaseResponse<UserChecklistResponse> response = controller.loadMyChecklist("u1");

        verify(useCase).execute("u1");
        assertThat(response.getResult()).isEqualTo(payload);
    }

    @Test
    void loadUserChecklist_ReturnsResponse() {
        UserChecklistResponse payload = UserChecklistResponse.builder().bedtime("23:00").build();
        when(useCase.execute("u2")).thenReturn(payload);

        BaseResponse<UserChecklistResponse> response = controller.loadUserChecklist("u2");

        verify(useCase).execute("u2");
        assertThat(response.getResult()).isEqualTo(payload);
    }
}
