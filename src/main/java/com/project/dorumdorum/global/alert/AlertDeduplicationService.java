package com.project.dorumdorum.global.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 동일한 알림이 짧은 시간 내에 반복 발송되는 것을 방지하는 서비스.
 * Redis TTL 기반으로 중복 여부를 판단한다.
 */
@Service
@RequiredArgsConstructor
public class AlertDeduplicationService {

    private static final String KEY_PREFIX = "alert:dedup:";

    private final StringRedisTemplate redisTemplate;

    /**
     * 해당 알림이 중복인지 확인하고, 중복이 아니면 TTL을 설정한다.
     *
     * @return true이면 중복 — 발송 건너뜀. false이면 신규 — 발송 진행.
     */
    public boolean isDuplicate(SystemAlert alert) {
        String key = buildKey(alert);
        Duration ttl = resolveTtl(alert.severity());
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, "1", ttl);
        return !Boolean.TRUE.equals(isNew);
    }

    private String buildKey(SystemAlert alert) {
        return KEY_PREFIX + alert.severity() + ":" + Math.abs(alert.title().hashCode());
    }

    private Duration resolveTtl(AlertSeverity severity) {
        return switch (severity) {
            case CRITICAL -> Duration.ofSeconds(60);
            case ERROR    -> Duration.ofSeconds(300);
            case WARN     -> Duration.ofSeconds(900);
            case INFO     -> Duration.ofSeconds(1800);
        };
    }
}
