package com.project.dorumdorum.global.alert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlertDeduplicationService Unit Tests")
class AlertDeduplicationServiceTest {

    @Mock private StringRedisTemplate redisTemplate;
    @Mock private ValueOperations<String, String> valueOps;
    @InjectMocks private AlertDeduplicationService service;

    private SystemAlert alert(AlertSeverity severity, String title) {
        return new SystemAlert(severity, AlertType.SYSTEM_HEALTH, title, "msg", Map.of());
    }

    @Test
    @DisplayName("신규 알림이면 false 반환 (Redis setIfAbsent = true)")
    void isDuplicate_WhenNew_ReturnsFalse() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(true);

        boolean result = service.isDuplicate(alert(AlertSeverity.CRITICAL, "서버 다운"));

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("중복 알림이면 true 반환 (Redis setIfAbsent = false)")
    void isDuplicate_WhenDuplicate_ReturnsTrue() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(false);

        boolean result = service.isDuplicate(alert(AlertSeverity.ERROR, "에러 발생"));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Redis setIfAbsent가 null 반환 시 중복으로 처리 (방어 로직)")
    void isDuplicate_WhenRedisReturnsNull_ReturnsTrueAsSafe() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(null);

        boolean result = service.isDuplicate(alert(AlertSeverity.WARN, "경고"));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("CRITICAL severity는 60초 TTL 사용")
    void isDuplicate_CriticalSeverity_Uses60SecondTtl() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(true);

        service.isDuplicate(alert(AlertSeverity.CRITICAL, "critical"));

        ArgumentCaptor<Duration> captor = ArgumentCaptor.forClass(Duration.class);
        verify(valueOps).setIfAbsent(anyString(), anyString(), captor.capture());
        assertThat(captor.getValue()).isEqualTo(Duration.ofSeconds(60));
    }

    @Test
    @DisplayName("ERROR severity는 300초 TTL 사용")
    void isDuplicate_ErrorSeverity_Uses300SecondTtl() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(true);

        service.isDuplicate(alert(AlertSeverity.ERROR, "error"));

        ArgumentCaptor<Duration> captor = ArgumentCaptor.forClass(Duration.class);
        verify(valueOps).setIfAbsent(anyString(), anyString(), captor.capture());
        assertThat(captor.getValue()).isEqualTo(Duration.ofSeconds(300));
    }

    @Test
    @DisplayName("동일 제목 + 다른 severity는 다른 Redis key 사용")
    void isDuplicate_SameTitleDifferentSeverity_UsesDifferentKeys() {
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), anyString(), any(Duration.class))).thenReturn(true);

        service.isDuplicate(alert(AlertSeverity.CRITICAL, "동일제목"));
        service.isDuplicate(alert(AlertSeverity.WARN, "동일제목"));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(valueOps, times(2)).setIfAbsent(captor.capture(), anyString(), any());
        assertThat(captor.getAllValues().get(0)).isNotEqualTo(captor.getAllValues().get(1));
    }
}
