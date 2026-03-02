package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("MarkAsReadNotificationUseCase 단위 테스트")
class MarkAsReadNotificationUseCaseTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MarkAsReadNotificationUseCase useCase;

    @Test
    @DisplayName("execute는 NotificationService.markAsRead에 위임한다")
    void execute_DelegatesToService() {
        // when
        useCase.execute("user-1", "n1");

        // then
        verify(notificationService).markAsRead("n1", "user-1");
    }
}

