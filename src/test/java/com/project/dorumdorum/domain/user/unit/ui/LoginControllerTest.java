package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.response.AuthTokenResponse;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoginUseCase;
import com.project.dorumdorum.domain.user.ui.LoginController;
import com.project.dorumdorum.global.properties.JwtProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private JwtProperties jwtProperties;

    @InjectMocks
    private LoginController controller;

    @Test
    @DisplayName("Should return access token in body and set refresh token cookie")
    void login_ReturnsAccessTokenAndSetsRefreshCookie() {
        LoginRequest request = new LoginRequest("test@university.ac.kr", "password123!");
        LoginResponse useCaseResponse = new LoginResponse("access", "refresh");
        when(loginUseCase.execute(request)).thenReturn(useCaseResponse);
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(3600L);

        ResponseEntity<AuthTokenResponse> response = controller.login(request);

        verify(loginUseCase).execute(request);
        assertThat(response.getBody()).isEqualTo(new AuthTokenResponse("access"));

        String setCookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(setCookieHeader).isNotNull();
        assertThat(setCookieHeader).contains("refresh");
        assertThat(setCookieHeader).contains("HttpOnly");
    }
}
