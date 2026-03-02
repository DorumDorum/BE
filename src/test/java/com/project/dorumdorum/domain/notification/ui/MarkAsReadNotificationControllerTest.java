package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.usecase.MarkAsReadNotificationUseCase;
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
@DisplayName("MarkAsReadNotificationController 단위 테스트")
class MarkAsReadNotificationControllerTest {

    @Mock
    private MarkAsReadNotificationUseCase useCase;

    @InjectMocks
    private MarkAsReadNotificationController controller;

    @Test
    @DisplayName("markAsReadNotification은 UseCase를 호출하고 204를 반환한다")
    void markAsReadNotification_DelegatesToUseCase() {
        // when
        ResponseEntity<Void> response = controller.markAsReadNotification("user-1", "n1");

        // then
        verify(useCase).execute("user-1", "n1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }
}

