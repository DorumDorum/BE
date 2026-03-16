package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.domain.service.NotificationDeviceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterDeviceTokenUseCase 단위 테스트")
class RegisterDeviceTokenUseCaseTest {

    @Mock
    private NotificationDeviceService notificationDeviceService;

    @InjectMocks
    private RegisterDeviceTokenUseCase useCase;

    @Test
    @DisplayName("execute는 디바이스 토큰 등록을 서비스에 위임한다")
    void execute_DelegatesToService() {
        useCase.execute("user-1", "device-1", "token-1");

        verify(notificationDeviceService).registerOrUpdateToken("user-1", "device-1", "token-1");
    }
}

