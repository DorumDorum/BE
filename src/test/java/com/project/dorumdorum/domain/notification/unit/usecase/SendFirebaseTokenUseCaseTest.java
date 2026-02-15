package com.project.dorumdorum.domain.notification.unit.usecase;

import com.project.dorumdorum.domain.notification.application.dto.request.SendFirebaseTokenRequest;
import com.project.dorumdorum.domain.notification.application.usecase.SendFirebaseTokenUseCase;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendFirebaseTokenUseCase Unit Tests")
class SendFirebaseTokenUseCaseTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SendFirebaseTokenUseCase useCase;

    @Test
    @DisplayName("Should delegate token save to notification service")
    void execute_DelegatesToService() {
        SendFirebaseTokenRequest request = new SendFirebaseTokenRequest("firebase-token");

        useCase.execute("u1", request);

        verify(notificationService).saveToken("u1", "firebase-token");
    }
}
