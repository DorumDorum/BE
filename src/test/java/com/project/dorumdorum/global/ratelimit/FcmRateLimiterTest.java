package com.project.dorumdorum.global.ratelimit;

import com.project.dorumdorum.global.properties.NotificationRateLimitProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FcmRateLimiter 단위 테스트")
class FcmRateLimiterTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private NotificationRateLimitProperties rateLimitProperties;

    @Test
    @DisplayName("활성화 상태면 슬라이딩 윈도우 스크립트를 실행한다")
    void isRateLimited_WhenEnabled_ExecutesSlidingWindowScript() {
        FcmRateLimiter rateLimiter = new FcmRateLimiter(stringRedisTemplate, rateLimitProperties);
        when(rateLimitProperties.isEnabled()).thenReturn(true);
        when(rateLimitProperties.getKey()).thenReturn("notification:fcm:rate-limit");
        when(rateLimitProperties.getPermitsPerWindow()).thenReturn(20L);
        when(rateLimitProperties.getWindowMillis()).thenReturn(10_000L);
        when(rateLimitProperties.getTtlSeconds()).thenReturn(20L);
        when(stringRedisTemplate.execute(
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(java.util.List.of("notification:fcm:rate-limit:user-1")),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("10000"),
                ArgumentMatchers.eq("20"),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("20")
        )).thenReturn(1L);

        boolean rateLimited = rateLimiter.isRateLimited("user-1");

        assertThat(rateLimited).isFalse();
        verify(stringRedisTemplate).execute(
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(java.util.List.of("notification:fcm:rate-limit:user-1")),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("10000"),
                ArgumentMatchers.eq("20"),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("20")
        );
    }

    @Test
    @DisplayName("비활성화 상태면 Redis를 호출하지 않는다")
    void isRateLimited_WhenDisabled_ReturnsFalse() {
        FcmRateLimiter rateLimiter = new FcmRateLimiter(stringRedisTemplate, rateLimitProperties);
        when(rateLimitProperties.isEnabled()).thenReturn(false);

        boolean rateLimited = rateLimiter.isRateLimited("user-1");

        assertThat(rateLimited).isFalse();
        verifyNoInteractions(stringRedisTemplate);
    }

    @Test
    @DisplayName("허용 횟수를 초과하면 레이트리밋 처리한다")
    void isRateLimited_WhenOverLimit_ReturnsTrue() {
        FcmRateLimiter rateLimiter = new FcmRateLimiter(stringRedisTemplate, rateLimitProperties);
        when(rateLimitProperties.isEnabled()).thenReturn(true);
        when(rateLimitProperties.getKey()).thenReturn("notification:fcm:rate-limit");
        when(rateLimitProperties.getPermitsPerWindow()).thenReturn(20L);
        when(rateLimitProperties.getWindowMillis()).thenReturn(10_000L);
        when(rateLimitProperties.getTtlSeconds()).thenReturn(20L);
        when(stringRedisTemplate.execute(
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(java.util.List.of("notification:fcm:rate-limit:user-1")),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("10000"),
                ArgumentMatchers.eq("20"),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("20")
        )).thenReturn(0L);

        boolean rateLimited = rateLimiter.isRateLimited("user-1");

        assertThat(rateLimited).isTrue();
    }
}
