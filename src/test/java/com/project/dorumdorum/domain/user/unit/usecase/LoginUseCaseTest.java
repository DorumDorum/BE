package com.project.dorumdorum.domain.user.unit.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoginUseCase;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.RefreshTokenService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.domain.user.fixture.RequestFixture;
import com.project.dorumdorum.domain.user.fixture.TokenFixture;
import com.project.dorumdorum.domain.user.fixture.UserFixture;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import com.project.dorumdorum.global.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginUseCase Unit Tests")
class LoginUseCaseTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private DomainEventLogger domainEventLogger;

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Test
    @DisplayName("Should return LoginResponse with valid credentials")
    void execute_WithValidCredentials_ReturnsLoginResponse() {
        // Arrange
        LoginRequest request = RequestFixture.createValidLoginRequest();
        User user = UserFixture.createUserWithEmail(request.email());
        String accessToken = TokenFixture.createValidAccessToken();
        String refreshToken = TokenFixture.createValidRefreshToken();
        Duration tokenExpiration = Duration.ofDays(7);

        when(userService.findByEmail(request.email())).thenReturn(user);
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(tokenProvider.createAccessToken(user.getUserNo(), user.getRole())).thenReturn(accessToken);
        when(tokenProvider.createRefreshToken(user.getUserNo(), user.getRole())).thenReturn(refreshToken);
        when(tokenProvider.getRemainingDuration(refreshToken)).thenReturn(Optional.of(tokenExpiration));

        // Act
        LoginResponse response = loginUseCase.execute(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo(accessToken);
        assertThat(response.refreshToken()).isEqualTo(refreshToken);

        verify(userService).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), user.getPassword());
        verify(tokenProvider).createAccessToken(user.getUserNo(), user.getRole());
        verify(tokenProvider).createRefreshToken(user.getUserNo(), user.getRole());
        verify(refreshTokenService).saveRefreshToken(user.getUserNo(), refreshToken, tokenExpiration);
    }

    @Test
    @DisplayName("Should throw LoginErrorException with invalid password")
    void execute_WithInvalidPassword_ThrowsLoginErrorException() {
        // Arrange
        LoginRequest request = RequestFixture.createLoginRequestWithWrongPassword();
        User user = UserFixture.createUserWithEmail(request.email());

        when(userService.findByEmail(request.email())).thenReturn(user);
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> loginUseCase.execute(request))
                .isInstanceOf(RestApiException.class);

        verify(userService).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), user.getPassword());
        verify(tokenProvider, never()).createAccessToken(anyString(), any(Role.class));
        verify(tokenProvider, never()).createRefreshToken(anyString(), any(Role.class));
        verify(refreshTokenService, never()).saveRefreshToken(anyString(), anyString(), any(Duration.class));
    }

    @Test
    @DisplayName("Should throw EmailNotFoundException with non-existent email")
    void execute_WithNonExistentEmail_ThrowsEmailNotFoundException() {
        // Arrange
        LoginRequest request = RequestFixture.createLoginRequestWithNonExistentEmail();

        when(userService.findByEmail(request.email())).thenThrow(RestApiException.class);

        // Act & Assert
        assertThatThrownBy(() -> loginUseCase.execute(request))
                .isInstanceOf(RestApiException.class);

        verify(userService).findByEmail(request.email());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(tokenProvider, never()).createAccessToken(anyString(), any(Role.class));
        verify(tokenProvider, never()).createRefreshToken(anyString(), any(Role.class));
    }

    @Test
    @DisplayName("Should save refresh token with valid login")
    void execute_WithValidLogin_SavesRefreshToken() {
        // Arrange
        LoginRequest request = RequestFixture.createValidLoginRequest();
        User user = UserFixture.createUserWithEmail(request.email());
        String accessToken = TokenFixture.createValidAccessToken();
        String refreshToken = TokenFixture.createValidRefreshToken();
        Duration tokenExpiration = Duration.ofDays(7);

        when(userService.findByEmail(request.email())).thenReturn(user);
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(tokenProvider.createAccessToken(user.getUserNo(), user.getRole())).thenReturn(accessToken);
        when(tokenProvider.createRefreshToken(user.getUserNo(), user.getRole())).thenReturn(refreshToken);
        when(tokenProvider.getRemainingDuration(refreshToken)).thenReturn(Optional.of(tokenExpiration));

        // Act
        loginUseCase.execute(request);

        // Assert
        verify(refreshTokenService).saveRefreshToken(
                eq(user.getUserNo()),
                eq(refreshToken),
                eq(tokenExpiration)
        );
    }

    @Test
    @DisplayName("Should throw ExpiredJwtException when refresh token is expired")
    void execute_WithExpiredRefreshToken_ThrowsExpiredJwtException() {
        // Arrange
        LoginRequest request = RequestFixture.createValidLoginRequest();
        User user = UserFixture.createUserWithEmail(request.email());
        String accessToken = TokenFixture.createValidAccessToken();
        String refreshToken = TokenFixture.createExpiredRefreshToken();

        when(userService.findByEmail(request.email())).thenReturn(user);
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(tokenProvider.createAccessToken(user.getUserNo(), user.getRole())).thenReturn(accessToken);
        when(tokenProvider.createRefreshToken(user.getUserNo(), user.getRole())).thenReturn(refreshToken);
        when(tokenProvider.getRemainingDuration(refreshToken)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> loginUseCase.execute(request))
                .isInstanceOf(RestApiException.class);

        verify(refreshTokenService, never()).saveRefreshToken(anyString(), anyString(), any(Duration.class));
    }
}
