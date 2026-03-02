package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.dto.request.RegisterDeviceTokenRequest;
import com.project.dorumdorum.domain.notification.application.usecase.RegisterDeviceTokenUseCase;
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
@DisplayName("RegisterDeviceTokenController 단위 테스트")
class RegisterDeviceTokenControllerTest {

    @Mock
    private RegisterDeviceTokenUseCase useCase;

    @InjectMocks
    private RegisterDeviceTokenController controller;

    @Test
    @DisplayName("registerDeviceToken은 UseCase를 호출하고 200 OK를 반환한다")
    void registerDeviceToken_DelegatesToUseCase() {
        // given
        RegisterDeviceTokenRequest request = new RegisterDeviceTokenRequest("device-1", "token-1");

        // when
        ResponseEntity<Void> response = controller.registerDeviceToken("user-1", request);

        // then
        verify(useCase).execute("user-1", "device-1", "token-1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}

