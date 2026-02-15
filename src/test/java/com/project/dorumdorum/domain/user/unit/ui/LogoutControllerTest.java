package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.usecase.LogoutUseCase;
import com.project.dorumdorum.domain.user.ui.LogoutController;
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
@DisplayName("LogoutController Unit Tests")
class LogoutControllerTest {

    @Mock
    private LogoutUseCase logoutUseCase;

    @InjectMocks
    private LogoutController controller;

    @Test
    @DisplayName("Should call logout use case and return success")
    void logout_CallsUseCaseAndReturnsSuccess() {
        String accessToken = "access-token";

        ResponseEntity<Void> response = controller.logout(accessToken);

        verify(logoutUseCase).execute(accessToken);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }
}
