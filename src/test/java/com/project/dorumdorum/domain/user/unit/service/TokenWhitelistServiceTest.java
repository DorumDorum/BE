package com.project.dorumdorum.domain.user.unit.service;

import com.project.dorumdorum.domain.user.domain.service.TokenWhitelistService;
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
@DisplayName("TokenWhitelistService Unit Tests")
class TokenWhitelistServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private TokenWhitelistService tokenWhitelistService;

    @Test
    @DisplayName("Should return true when whitelist token exists and matches")
    void isWhitelistToken_WithMatchingToken_ReturnsTrue() {
        // Arrange
        String token = "token";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("WHITELIST:" + token)).thenReturn(token);

        // Act
        boolean result = tokenWhitelistService.isWhitelistToken(token);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when whitelist token does not exist")
    void isWhitelistToken_WithMissingToken_ReturnsFalse() {
        // Arrange
        String token = "missing";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("WHITELIST:" + token)).thenReturn(null);

        // Act
        boolean result = tokenWhitelistService.isWhitelistToken(token);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should save whitelist token with timeout")
    void whitelist_SavesTokenWithTimeout() {
        // Arrange
        String token = "token";
        Duration timeout = Duration.ofMinutes(10);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        tokenWhitelistService.whitelist(token, timeout);

        // Assert
        verify(valueOperations).set("WHITELIST:" + token, "", timeout);
    }

    @Test
    @DisplayName("Should delete whitelist token by key")
    void deleteWhitelistToken_DeletesKey() {
        // Arrange
        String token = "token";

        // Act
        tokenWhitelistService.deleteWhitelistToken(token);

        // Assert
        verify(redisTemplate).delete("WHITELIST:" + token);
    }
}
