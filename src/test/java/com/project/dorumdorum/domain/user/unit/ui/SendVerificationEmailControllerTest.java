package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.usecase.SendVerificationEmailUseCase;
import com.project.dorumdorum.domain.user.ui.SendVerificationEmailController;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendVerificationEmailController Unit Tests")
class SendVerificationEmailControllerTest {

    @Mock
    private SendVerificationEmailUseCase sendVerificationEmailUseCase;

    @InjectMocks
    private SendVerificationEmailController controller;

    @Test
    @DisplayName("Should call use case and return success response")
    void send_CallsUseCaseAndReturnsSuccess() {
        String email = "test@university.ac.kr";

        ResponseEntity<Void> response = controller.send(email);

        verify(sendVerificationEmailUseCase).send(email);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }
}
