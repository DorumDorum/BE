package com.project.dorumdorum.domain.user.unit.service;

import com.project.dorumdorum.domain.user.domain.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenService Unit Tests")
class RefreshTokenServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("Should save refresh token with timeout")
    void saveRefreshToken_SavesValueWithTimeout() {
        // Arrange
        String userNo = "0000000000000001";
        String refreshToken = "refresh-token";
        Duration timeout = Duration.ofDays(7);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        refreshTokenService.saveRefreshToken(userNo, refreshToken, timeout);

        // Assert
        verify(valueOperations).set("REFRESH_TOKEN:" + userNo, refreshToken, timeout);
    }

    @Test
    @DisplayName("Should delete refresh token by user number")
    void deleteRefreshToken_DeletesStoredToken() {
        // Arrange
        String userNo = "0000000000000001";

        // Act
        refreshTokenService.deleteRefreshToken(userNo);

        // Assert
        verify(redisTemplate).delete("REFRESH_TOKEN:" + userNo);
    }

    @Test
    @DisplayName("Should return true when token exists and matches")
    void isExist_WithMatchingToken_ReturnsTrue() {
        // Arrange
        String userNo = "0000000000000001";
        String token = "refresh-token";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("REFRESH_TOKEN:" + userNo)).thenReturn(token);

        // Act
        boolean result = refreshTokenService.isExist(token, userNo);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when token does not exist")
    void isExist_WithMissingToken_ReturnsFalse() {
        // Arrange
        String userNo = "0000000000000001";
        String token = "refresh-token";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("REFRESH_TOKEN:" + userNo)).thenReturn(null);

        // Act
        boolean result = refreshTokenService.isExist(token, userNo);

        // Assert
        assertThat(result).isFalse();
    }
}
