package com.project.dorumdorum.domain.user.unit.service;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.RefreshTokenService;
import com.project.dorumdorum.domain.user.domain.service.TokenReissueService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
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

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenReissueService Unit Tests")
class TokenReissueServiceTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserService userService;
    @Mock
    private DomainEventLogger domainEventLogger;

    @InjectMocks
    private TokenReissueService tokenReissueService;

    @Test
    @DisplayName("Should return new tokens with valid refresh token")
    void reissue_WithValidRefreshToken_ReturnsNewTokens() {
        // Arrange
        String userNo = "0000000000000001";
        String oldRefreshToken = TokenFixture.createValidRefreshToken();
        String newAccessToken = TokenFixture.createValidAccessToken();
        String newRefreshToken = TokenFixture.createValidRefreshToken();
        User user = UserFixture.createUserWithId(userNo);
        Duration duration = Duration.ofDays(7);

        when(refreshTokenService.isExist(oldRefreshToken, userNo)).thenReturn(true);
        when(userService.findById(userNo)).thenReturn(user);
        when(tokenProvider.createAccessToken(userNo, user.getRole())).thenReturn(newAccessToken);
        when(tokenProvider.createRefreshToken(userNo, user.getRole())).thenReturn(newRefreshToken);
        when(tokenProvider.getRemainingDuration(oldRefreshToken)).thenReturn(Optional.of(duration));

        // Act
        TokenReissueResponse response = tokenReissueService.reissue(oldRefreshToken, userNo);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo(newAccessToken);
        assertThat(response.refreshToken()).isEqualTo(newRefreshToken);

        verify(refreshTokenService).isExist(oldRefreshToken, userNo);
        verify(refreshTokenService).deleteRefreshToken(userNo);
        verify(userService).findById(userNo);
        verify(tokenProvider).createAccessToken(userNo, user.getRole());
        verify(tokenProvider).createRefreshToken(userNo, user.getRole());
        verify(refreshTokenService).saveRefreshToken(userNo, newRefreshToken, duration);
    }

    @Test
    @DisplayName("Should throw InvalidRefreshTokenException with invalid refresh token")
    void reissue_WithInvalidRefreshToken_ThrowsInvalidRefreshTokenException() {
        // Arrange
        String userNo = "0000000000000001";
        String invalidToken = TokenFixture.createInvalidToken();

        when(refreshTokenService.isExist(invalidToken, userNo)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> tokenReissueService.reissue(invalidToken, userNo))
                .isInstanceOf(RestApiException.class);

        verify(refreshTokenService).isExist(invalidToken, userNo);
        verify(refreshTokenService, never()).deleteRefreshToken(anyString());
        verify(userService, never()).findById(anyString());
        verify(tokenProvider, never()).createAccessToken(anyString(), any(Role.class));
        verify(tokenProvider, never()).createRefreshToken(anyString(), any(Role.class));
    }

    @Test
    @DisplayName("Should throw InvalidRefreshTokenException with non-existent token")
    void reissue_WithNonExistentToken_ThrowsInvalidRefreshTokenException() {
        // Arrange
        String userNo = "0000000000000001";
        String nonExistentToken = TokenFixture.createValidRefreshToken();

        when(refreshTokenService.isExist(nonExistentToken, userNo)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> tokenReissueService.reissue(nonExistentToken, userNo))
                .isInstanceOf(RestApiException.class);

        verify(refreshTokenService).isExist(nonExistentToken, userNo);
        verify(refreshTokenService, never()).deleteRefreshToken(anyString());
    }

    @Test
    @DisplayName("Should delete old refresh token with valid token")
    void reissue_WithValidToken_DeletesOldRefreshToken() {
        // Arrange
        String userNo = "0000000000000001";
        String oldRefreshToken = TokenFixture.createValidRefreshToken();
        String newAccessToken = TokenFixture.createValidAccessToken();
        String newRefreshToken = TokenFixture.createValidRefreshToken();
        User user = UserFixture.createUserWithId(userNo);
        Duration duration = Duration.ofDays(7);

        when(refreshTokenService.isExist(oldRefreshToken, userNo)).thenReturn(true);
        when(userService.findById(userNo)).thenReturn(user);
        when(tokenProvider.createAccessToken(userNo, user.getRole())).thenReturn(newAccessToken);
        when(tokenProvider.createRefreshToken(userNo, user.getRole())).thenReturn(newRefreshToken);
        when(tokenProvider.getRemainingDuration(oldRefreshToken)).thenReturn(Optional.of(duration));

        // Act
        tokenReissueService.reissue(oldRefreshToken, userNo);

        // Assert
        verify(refreshTokenService).deleteRefreshToken(userNo);
    }

    @Test
    @DisplayName("Should save new refresh token with valid token")
    void reissue_WithValidToken_SavesNewRefreshToken() {
        // Arrange
        String userNo = "0000000000000001";
        String oldRefreshToken = TokenFixture.createValidRefreshToken();
        String newAccessToken = TokenFixture.createValidAccessToken();
        String newRefreshToken = TokenFixture.createValidRefreshToken();
        User user = UserFixture.createUserWithId(userNo);
        Duration duration = Duration.ofDays(7);

        when(refreshTokenService.isExist(oldRefreshToken, userNo)).thenReturn(true);
        when(userService.findById(userNo)).thenReturn(user);
        when(tokenProvider.createAccessToken(userNo, user.getRole())).thenReturn(newAccessToken);
        when(tokenProvider.createRefreshToken(userNo, user.getRole())).thenReturn(newRefreshToken);
        when(tokenProvider.getRemainingDuration(oldRefreshToken)).thenReturn(Optional.of(duration));

        // Act
        tokenReissueService.reissue(oldRefreshToken, userNo);

        // Assert
        verify(refreshTokenService).saveRefreshToken(userNo, newRefreshToken, duration);
    }

    @Test
    @DisplayName("Should throw ExpiredJwtException with expired token")
    void reissue_WithExpiredToken_ThrowsExpiredJwtException() {
        // Arrange
        String userNo = "0000000000000001";
        String expiredToken = TokenFixture.createExpiredRefreshToken();
        User user = UserFixture.createUserWithId(userNo);

        when(refreshTokenService.isExist(expiredToken, userNo)).thenReturn(true);
        when(userService.findById(userNo)).thenReturn(user);
        when(tokenProvider.createAccessToken(userNo, user.getRole())).thenReturn(TokenFixture.createValidAccessToken());
        when(tokenProvider.createRefreshToken(userNo, user.getRole())).thenReturn(TokenFixture.createValidRefreshToken());
        when(tokenProvider.getRemainingDuration(expiredToken)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> tokenReissueService.reissue(expiredToken, userNo))
                .isInstanceOf(RestApiException.class);

        verify(refreshTokenService).deleteRefreshToken(userNo);
        verify(refreshTokenService, never()).saveRefreshToken(anyString(), anyString(), any(Duration.class));
    }

    @Test
    @DisplayName("Should preserve user role with valid token")
    void reissue_WithValidToken_PreservesUserRole() {
        // Arrange
        String userNo = "0000000000000001";
        String oldRefreshToken = TokenFixture.createValidRefreshToken();
        User user = UserFixture.createUserWithId(userNo);
        Duration duration = Duration.ofDays(7);

        when(refreshTokenService.isExist(oldRefreshToken, userNo)).thenReturn(true);
        when(userService.findById(userNo)).thenReturn(user);
        when(tokenProvider.createAccessToken(userNo, user.getRole())).thenReturn(TokenFixture.createValidAccessToken());
        when(tokenProvider.createRefreshToken(userNo, user.getRole())).thenReturn(TokenFixture.createValidRefreshToken());
        when(tokenProvider.getRemainingDuration(oldRefreshToken)).thenReturn(Optional.of(duration));

        // Act
        tokenReissueService.reissue(oldRefreshToken, userNo);

        // Assert
        verify(tokenProvider).createAccessToken(userNo, user.getRole());
        verify(tokenProvider).createRefreshToken(userNo, user.getRole());
    }
}
