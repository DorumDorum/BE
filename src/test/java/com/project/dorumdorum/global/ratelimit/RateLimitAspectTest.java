package com.project.dorumdorum.global.ratelimit;

import com.project.dorumdorum.global.exception.RestApiException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RateLimitAspect 단위 테스트")
class RateLimitAspectTest {

    @Mock
    private SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Mock
    private RateLimitPolicyRegistry rateLimitPolicyRegistry;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @Test
    @DisplayName("레이트리밋에 걸리면 예외를 던진다")
    void applyRateLimit_WhenDenied_ThrowsException() throws Throwable {
        RateLimitAspect aspect = new RateLimitAspect(slidingWindowRateLimiter, rateLimitPolicyRegistry);
        RateLimitRule rule = new RateLimitRule(20L, 10_000L, 20L);
        Method method = Target.class.getMethod("call", String.class);

        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"user-1"});
        when(rateLimitPolicyRegistry.get("fcm")).thenReturn(rule);
        when(slidingWindowRateLimiter.isRateLimited("fcm", "user-1", rule)).thenReturn(true);

        assertThatThrownBy(() -> aspect.applyRateLimit(joinPoint))
                .isInstanceOf(RestApiException.class);

        verify(joinPoint, never()).proceed();
    }

    static class Target {
        @RateLimited(tag = "fcm", key = "#p0")
        public void call(String userNo) {
        }
    }
}
