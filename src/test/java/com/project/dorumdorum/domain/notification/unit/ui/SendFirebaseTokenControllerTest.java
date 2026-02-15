package com.project.dorumdorum.domain.notification.unit.ui;

import com.project.dorumdorum.domain.notification.application.dto.request.SendFirebaseTokenRequest;
import com.project.dorumdorum.domain.notification.application.usecase.SendFirebaseTokenUseCase;
import com.project.dorumdorum.domain.notification.ui.SendFirebaseTokenController;
import com.project.dorumdorum.global.common.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendFirebaseTokenController Unit Tests")
class SendFirebaseTokenControllerTest {

    @Mock
    private SendFirebaseTokenUseCase useCase;

    @InjectMocks
    private SendFirebaseTokenController controller;

    @Test
    @DisplayName("Should call usecase and return success response")
    void sendFirebaseToken_CallsUseCase() {
        SendFirebaseTokenRequest request = new SendFirebaseTokenRequest("firebase-token");

        BaseResponse<Void> response = controller.sendFirebaseToken("u1", request);

        verify(useCase).execute("u1", request);
        assertThat(response.getCode()).isEqualTo("COMMON200");
    }
}
