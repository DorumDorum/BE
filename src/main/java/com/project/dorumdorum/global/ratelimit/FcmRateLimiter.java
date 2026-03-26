package com.project.dorumdorum.global.ratelimit;

import com.project.dorumdorum.global.properties.NotificationRateLimitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FcmRateLimiter {

    private static final DefaultRedisScript<Long> SLIDING_WINDOW_SCRIPT = createScript();

    private final StringRedisTemplate stringRedisTemplate;
    private final NotificationRateLimitProperties rateLimitProperties;

    public boolean isRateLimited(String userNo) {
        if (!rateLimitProperties.isEnabled()) {
            return false;
        }

        String key = buildUserKey(userNo);
        long nowMs = System.currentTimeMillis();
        String requestNo = nowMs + "-" + UUID.randomUUID();
        String now = String.valueOf(nowMs);
        String windowMillis = String.valueOf(rateLimitProperties.getWindowSeconds() * 1000L);
        String permits = String.valueOf(rateLimitProperties.getPermitsPerWindow());
        String ttlSeconds = String.valueOf(rateLimitProperties.getWindowSeconds() + 1);

        boolean allowed = stringRedisTemplate.execute(  // Redis Sorted Set으로 슬라이딩 윈도우 구현
                SLIDING_WINDOW_SCRIPT,  // 슬라이딩 윈도우 스크립트
                List.of(key),   // 키
                now,    // 현재 시점
                windowMillis,   // 집계 구간
                permits,    // 요청 횟수
                requestNo,  // 요청
                ttlSeconds  // TTL
        ) == 1L;

        return !allowed;
    }

    private String buildUserKey(String userNo) {
        return rateLimitProperties.getKey() + ":" + userNo;
    }

    private static DefaultRedisScript<Long> createScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptText("""
                local key = KEYS[1]
                local nowMs = tonumber(ARGV[1])
                local windowMs = tonumber(ARGV[2])
                local limit = tonumber(ARGV[3])
                local requestNo = ARGV[4]
                local ttlSeconds = tonumber(ARGV[5])
                local minScore = nowMs - windowMs

                redis.call('ZREMRANGEBYSCORE', key, 0, minScore)

                local requestCount = redis.call('ZCARD', key)
                if requestCount >= limit then
                    redis.call('EXPIRE', key, ttlSeconds)
                    return 0
                end

                redis.call('ZADD', key, nowMs, requestNo)
                redis.call('EXPIRE', key, ttlSeconds)
                return 1
                """);
        return script;
    }
}
