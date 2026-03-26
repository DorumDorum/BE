package com.project.dorumdorum.global.ratelimit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SlidingWindowRateLimiter 단위 테스트")
class SlidingWindowRateLimiterTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @DisplayName("허용되면 false를 반환한다")
    void isRateLimited_WhenAllowed_ReturnsFalse() {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(stringRedisTemplate);
        RateLimitRule rule = new RateLimitRule(20L, 10_000L, 20L);
        when(stringRedisTemplate.execute(
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(java.util.List.of("rate-limit:fcm:user-1")),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("10000"),
                ArgumentMatchers.eq("20"),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("20")
        )).thenReturn(1L);

        boolean rateLimited = limiter.isRateLimited("fcm", "user-1", rule);

        assertThat(rateLimited).isFalse();
        verify(stringRedisTemplate).execute(
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(java.util.List.of("rate-limit:fcm:user-1")),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("10000"),
                ArgumentMatchers.eq("20"),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("20")
        );
    }

    @Test
    @DisplayName("초과되면 true를 반환한다")
    void isRateLimited_WhenDenied_ReturnsTrue() {
        SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(stringRedisTemplate);
        RateLimitRule rule = new RateLimitRule(20L, 10_000L, 20L);
        when(stringRedisTemplate.execute(
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(java.util.List.of("rate-limit:fcm:user-1")),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("10000"),
                ArgumentMatchers.eq("20"),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq("20")
        )).thenReturn(0L);

        boolean rateLimited = limiter.isRateLimited("fcm", "user-1", rule);

        assertThat(rateLimited).isTrue();
    }
}
