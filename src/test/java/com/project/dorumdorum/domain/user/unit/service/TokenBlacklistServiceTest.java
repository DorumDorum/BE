package com.project.dorumdorum.domain.user.unit.service;

import com.project.dorumdorum.domain.user.domain.service.TokenBlacklistService;
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
@DisplayName("TokenBlacklistService Unit Tests")
class TokenBlacklistServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private TokenBlacklistService tokenBlacklistService;

    @Test
    @DisplayName("Should return true when blacklist token exists and matches")
    void isBlacklistToken_WithMatchingToken_ReturnsTrue() {
        // Arrange
        String token = "token";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("BLACKLIST:" + token)).thenReturn(token);

        // Act
        boolean result = tokenBlacklistService.isBlacklistToken(token);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when blacklist token does not exist")
    void isBlacklistToken_WithMissingToken_ReturnsFalse() {
        // Arrange
        String token = "missing";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("BLACKLIST:" + token)).thenReturn(null);

        // Act
        boolean result = tokenBlacklistService.isBlacklistToken(token);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should save blacklist token with expiration")
    void blacklist_SavesTokenWithExpiration() {
        // Arrange
        String token = "token";
        Duration expiration = Duration.ofMinutes(30);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        tokenBlacklistService.blacklist(token, expiration);

        // Assert
        verify(valueOperations).set("BLACKLIST:" + token, "", expiration);
    }
}
