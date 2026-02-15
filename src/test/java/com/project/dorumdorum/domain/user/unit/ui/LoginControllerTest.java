package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoginUseCase;
import com.project.dorumdorum.domain.user.ui.LoginController;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginController Unit Tests")
class LoginControllerTest {

    @Mock
    private LoginUseCase loginUseCase;

    @InjectMocks
    private LoginController controller;

    @Test
    @DisplayName("Should return login response from use case")
    void login_ReturnsUseCaseResult() {
        LoginRequest request = new LoginRequest("test@university.ac.kr", "password123!");
        LoginResponse expected = new LoginResponse("access", "refresh");
        when(loginUseCase.execute(request)).thenReturn(expected);

        ResponseEntity<LoginResponse> response = controller.login(request);

        verify(loginUseCase).execute(request);
        assertThat(response.getBody()).isEqualTo(expected);
    }
}
