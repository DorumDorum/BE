package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.dto.response.LoadNotificationResponse;
import com.project.dorumdorum.domain.notification.application.usecase.LoadMyNotificationsUseCase;
import com.project.dorumdorum.global.pagination.CursorPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadMyNotificationsController 단위 테스트")
class LoadMyNotificationsControllerTest {

    @Mock
    private LoadMyNotificationsUseCase useCase;

    @InjectMocks
    private LoadMyNotificationsController controller;

    @Test
    @DisplayName("loadMyNotifications는 UseCase 결과를 그대로 반환한다")
    void loadMyNotifications_ReturnsUseCaseResult() {
        CursorPage<LoadNotificationResponse> page =
                new CursorPage<>(List.of(), null, false);
        when(useCase.execute("user-1", null)).thenReturn(page);

        ResponseEntity<CursorPage<LoadNotificationResponse>> response =
                controller.loadMyNotifications("user-1", null);

        verify(useCase).execute("user-1", null);
        assertThat(response.getBody()).isEqualTo(page);
    }
}

