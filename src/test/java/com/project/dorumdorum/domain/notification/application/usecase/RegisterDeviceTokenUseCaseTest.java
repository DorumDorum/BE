package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.domain.repository.UserDeviceTokenRepository;
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
    private UserDeviceTokenRepository userDeviceTokenRepository;

    @InjectMocks
    private RegisterDeviceTokenUseCase useCase;

    @Test
    @DisplayName("execute는 UserDeviceTokenRepository.save에 위임한다")
    void execute_DelegatesToRepository() {
        // when
        useCase.execute("user-1", "device-1", "token-1");

        // then
        verify(userDeviceTokenRepository).save("user-1", "device-1", "token-1");
    }
}

