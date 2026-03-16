package com.project.dorumdorum.global.ratelimit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RedisTokenBucketRateLimiter 단위 테스트")
class RedisTokenBucketRateLimiterTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @DisplayName("스크립트 결과가 1이면 permit 획득 성공을 반환한다")
    void tryAcquire_WhenScriptReturnsOne_ReturnsTrue() {
        RedisTokenBucketRateLimiter limiter = new RedisTokenBucketRateLimiter(stringRedisTemplate);
        when(stringRedisTemplate.execute(
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(List.of("notification:fcm")),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(1L);

        boolean acquired = limiter.tryAcquire("notification:fcm", 100, 20, 1);

        assertThat(acquired).isTrue();
    }

    @Test
    @DisplayName("스크립트 결과가 0이면 permit 획득 실패를 반환한다")
    void tryAcquire_WhenScriptReturnsZero_ReturnsFalse() {
        RedisTokenBucketRateLimiter limiter = new RedisTokenBucketRateLimiter(stringRedisTemplate);
        when(stringRedisTemplate.execute(
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(List.of("notification:fcm")),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(0L);

        boolean acquired = limiter.tryAcquire("notification:fcm", 100, 20, 1);

        assertThat(acquired).isFalse();
    }

    @Test
    @DisplayName("잘못된 인자면 Redis 실행 없이 실패를 반환한다")
    void tryAcquire_WhenInvalidArguments_ReturnsFalseWithoutRedisCall() {
        RedisTokenBucketRateLimiter limiter = new RedisTokenBucketRateLimiter(stringRedisTemplate);

        boolean acquired = limiter.tryAcquire("", 0, 0, 0);

        assertThat(acquired).isFalse();
        verify(stringRedisTemplate, never()).execute(
                ArgumentMatchers.any(),
                ArgumentMatchers.<List<String>>any(),
                ArgumentMatchers.<Object>any()
        );
    }
}
