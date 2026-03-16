package com.project.dorumdorum.global.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisTokenBucketRateLimiter implements DistributedRateLimiter {

    private static final DefaultRedisScript<Long> TOKEN_BUCKET_SCRIPT = createScript();

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean tryAcquire(String key, long capacity, long refillTokensPerSecond, long requestedPermits) {
        if (key == null || key.isBlank()) {
            return false;
        }

        if (capacity <= 0 || refillTokensPerSecond <= 0 || requestedPermits <= 0) {
            return false;
        }

        Long result = stringRedisTemplate.execute(
                TOKEN_BUCKET_SCRIPT,
                List.of(key),
                String.valueOf(capacity),
                String.valueOf(refillTokensPerSecond),
                String.valueOf(requestedPermits),
                String.valueOf(System.currentTimeMillis())
        );

        return Long.valueOf(1L).equals(result);
    }

    private static DefaultRedisScript<Long> createScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptText("""
                local key = KEYS[1]
                local capacity = tonumber(ARGV[1])
                local refillRate = tonumber(ARGV[2])
                local requested = tonumber(ARGV[3])
                local nowMs = tonumber(ARGV[4])

                local values = redis.call('HMGET', key, 'tokens', 'lastRefillMs')
                local tokens = tonumber(values[1])
                local lastRefillMs = tonumber(values[2])

                if tokens == nil then
                    tokens = capacity
                end
                if lastRefillMs == nil then
                    lastRefillMs = nowMs
                end

                local elapsedMs = math.max(0, nowMs - lastRefillMs)
                local refill = math.floor((elapsedMs * refillRate) / 1000)
                if refill > 0 then
                    tokens = math.min(capacity, tokens + refill)
                end

                local allowed = 0
                if tokens >= requested then
                    tokens = tokens - requested
                    allowed = 1
                end

                redis.call('HSET', key, 'tokens', tokens, 'lastRefillMs', nowMs)

                local ttlSeconds = math.max(1, math.ceil((capacity * 2) / refillRate))
                redis.call('EXPIRE', key, ttlSeconds)

                return allowed
                """);
        return script;
    }
}
