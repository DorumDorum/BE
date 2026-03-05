package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoginUseCase;
import com.project.dorumdorum.domain.user.ui.LoginController;
import com.project.dorumdorum.global.security.cookie.AuthCookieWriter;
import jakarta.servlet.http.HttpServletResponse;
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

    @Mock
    private AuthCookieWriter authCookieWriter;

    @InjectMocks
    private LoginController controller;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("Should delegate to use case and write cookies via AuthCookieWriter")
    void login_DelegatesToUseCaseAndWritesCookies() {
        LoginRequest request = new LoginRequest("test@university.ac.kr", "password123!");
        LoginResponse useCaseResponse = new LoginResponse("access", "refresh");
        when(loginUseCase.execute(request)).thenReturn(useCaseResponse);

        ResponseEntity<Void> result = controller.login(response, request);

        verify(loginUseCase).execute(request);
        verify(authCookieWriter).writeAccessCookie(response, "access");
        verify(authCookieWriter).writeRefreshCookie(response, "refresh");
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isNull();
    }
}
