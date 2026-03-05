package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.usecase.LogoutUseCase;
import com.project.dorumdorum.domain.user.ui.LogoutController;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("LogoutController Unit Tests")
class LogoutControllerTest {

    @Mock
    private LogoutUseCase logoutUseCase;

    @Mock
    private AuthCookieWriter authCookieWriter;

    @InjectMocks
    private LogoutController controller;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("Should call logout use case, expire cookies and return success")
    void logout_CallsUseCaseExpiresCookiesAndReturnsSuccess() {
        String accessToken = "access-token";

        ResponseEntity<Void> result = controller.logout(response, accessToken);

        verify(logoutUseCase).execute(accessToken);
        verify(authCookieWriter).expireAuthCookies(response);
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isNull();
    }
}
