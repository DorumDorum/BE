package com.project.dorumdorum.domain.user.unit.usecase;

import com.project.dorumdorum.domain.user.application.usecase.LogoutUseCase;
import com.project.dorumdorum.domain.user.domain.service.RefreshTokenService;
import com.project.dorumdorum.domain.user.domain.service.TokenBlacklistService;
import com.project.dorumdorum.domain.user.domain.service.TokenWhitelistService;
import com.project.dorumdorum.domain.user.fixture.TokenFixture;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LogoutUseCase Unit Tests")
class LogoutUseCaseTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @Mock
    private TokenWhitelistService tokenWhitelistService;

    @InjectMocks
    private LogoutUseCase logoutUseCase;

    @Test
    @DisplayName("Should delete whitelist token with valid token")
    void execute_WithValidToken_DeletesWhitelistToken() {
        // Arrange
        String accessToken = TokenFixture.createValidAccessToken();
        String userNo = "0000000000000001";
        Duration expiration = Duration.ofHours(1);

        when(tokenProvider.getId(accessToken)).thenReturn(Optional.of(userNo));
        when(tokenProvider.getRemainingDuration(accessToken)).thenReturn(Optional.of(expiration));

        // Act
        logoutUseCase.execute(accessToken);

        // Assert
        verify(tokenWhitelistService).deleteWhitelistToken(accessToken);
    }

    @Test
    @DisplayName("Should delete refresh token with valid token")
    void execute_WithValidToken_DeletesRefreshToken() {
        // Arrange
        String accessToken = TokenFixture.createValidAccessToken();
        String userNo = "0000000000000001";
        Duration expiration = Duration.ofHours(1);

        when(tokenProvider.getId(accessToken)).thenReturn(Optional.of(userNo));
        when(tokenProvider.getRemainingDuration(accessToken)).thenReturn(Optional.of(expiration));

        // Act
        logoutUseCase.execute(accessToken);

        // Assert
        verify(refreshTokenService).deleteRefreshToken(userNo);
    }

    @Test
    @DisplayName("Should add token to blacklist with valid token")
    void execute_WithValidToken_AddsTokenToBlacklist() {
        // Arrange
        String accessToken = TokenFixture.createValidAccessToken();
        String userNo = "0000000000000001";
        Duration expiration = Duration.ofHours(1);

        when(tokenProvider.getId(accessToken)).thenReturn(Optional.of(userNo));
        when(tokenProvider.getRemainingDuration(accessToken)).thenReturn(Optional.of(expiration));

        // Act
        logoutUseCase.execute(accessToken);

        // Assert
        verify(tokenBlacklistService).blacklist(accessToken, expiration);
    }

    @Test
    @DisplayName("Should throw InvalidIdTokenException with invalid token")
    void execute_WithInvalidToken_ThrowsInvalidIdTokenException() {
        // Arrange
        String invalidToken = TokenFixture.createInvalidToken();

        when(tokenProvider.getId(invalidToken)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> logoutUseCase.execute(invalidToken))
                .isInstanceOf(RestApiException.class);

        verify(tokenProvider).getId(invalidToken);
        verify(tokenWhitelistService, never()).deleteWhitelistToken(anyString());
        verify(refreshTokenService, never()).deleteRefreshToken(anyString());
        verify(tokenBlacklistService, never()).blacklist(anyString(), any(Duration.class));
    }

    @Test
    @DisplayName("Should throw InvalidAccessTokenException with expired token")
    void execute_WithExpiredToken_ThrowsInvalidAccessTokenException() {
        // Arrange
        String expiredToken = TokenFixture.createExpiredAccessToken();
        String userNo = "0000000000000001";

        when(tokenProvider.getId(expiredToken)).thenReturn(Optional.of(userNo));
        when(tokenProvider.getRemainingDuration(expiredToken)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> logoutUseCase.execute(expiredToken))
                .isInstanceOf(RestApiException.class);

        verify(tokenProvider).getId(expiredToken);
        verify(tokenProvider).getRemainingDuration(expiredToken);
        verify(tokenWhitelistService, never()).deleteWhitelistToken(anyString());
        verify(refreshTokenService, never()).deleteRefreshToken(anyString());
        verify(tokenBlacklistService, never()).blacklist(anyString(), any(Duration.class));
    }

    @Test
    @DisplayName("Should blacklist with correct duration")
    void execute_WithValidToken_BlacklistsWithCorrectDuration() {
        // Arrange
        String accessToken = TokenFixture.createValidAccessToken();
        String userNo = "0000000000000001";
        Duration expectedExpiration = Duration.ofHours(2);

        when(tokenProvider.getId(accessToken)).thenReturn(Optional.of(userNo));
        when(tokenProvider.getRemainingDuration(accessToken)).thenReturn(Optional.of(expectedExpiration));

        // Act
        logoutUseCase.execute(accessToken);

        // Assert
        verify(tokenBlacklistService).blacklist(accessToken, expectedExpiration);

        // Verify correct execution order
        InOrder inOrder = inOrder(tokenWhitelistService, refreshTokenService, tokenBlacklistService);
        inOrder.verify(tokenWhitelistService).deleteWhitelistToken(accessToken);
        inOrder.verify(refreshTokenService).deleteRefreshToken(userNo);
        inOrder.verify(tokenBlacklistService).blacklist(accessToken, expectedExpiration);
    }
}
