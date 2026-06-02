package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.usecase.MarkAllAsReadNotificationsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("MarkAllAsReadNotificationsController 단위 테스트")
class MarkAllAsReadNotificationsControllerTest {

    @Mock
    private MarkAllAsReadNotificationsUseCase useCase;

    @InjectMocks
    private MarkAllAsReadNotificationsController controller;

    @Test
    @DisplayName("markAllAsReadNotifications는 UseCase를 호출하고 204를 반환한다")
    void markAllAsReadNotifications_DelegatesToUseCase() {
        // when
        ResponseEntity<Void> response = controller.markAllAsReadNotifications("user-1");

        // then
        verify(useCase).execute("user-1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }
}
