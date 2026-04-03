package com.project.dorumdorum.global.warmer;

import com.project.dorumdorum.global.alert.AlertSeverity;
import com.project.dorumdorum.global.alert.AlertType;
import com.project.dorumdorum.global.alert.SystemAlertPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarmupScheduler {

    private static final Duration IDLE_THRESHOLD = Duration.ofMinutes(10);
    private static final Duration COOLDOWN = Duration.ofMinutes(30);

    private final RequestActivityTrackingFilter requestActivityTrackingFilter;
    private final WarmupService warmupService;
    private final SystemAlertPublisher systemAlertPublisher;

    private final AtomicLong lastWarmupTime = new AtomicLong(0L);

    @Scheduled(fixedDelay = 60_000)
    public void warmIfIdle() {
        long now = System.currentTimeMillis();
        long lastRequest = requestActivityTrackingFilter.getLastRequestTime();
        long lastWarm = lastWarmupTime.get();

        if (!isIdle(now, lastRequest)) {
            return;
        }
        if (!isCooldownPassed(now, lastWarm)) {
            return;
        }

        log.info("[Warmup] Service idle for {} ms, running warmup", now - lastRequest);
        try {
            warmupService.warm();
            lastWarmupTime.set(now);
        } catch (Exception e) {
            log.error("[Warmup] Warmup 실패", e);
            systemAlertPublisher.publish(
                    AlertSeverity.WARN,
                    AlertType.SCHEDULED_JOB,
                    "[Warmup] Warmup 작업 실패",
                    e.getMessage()
            );
        }
    }

    private boolean isIdle(long now, long lastRequest) {
        return now - lastRequest >= IDLE_THRESHOLD.toMillis();
    }

    private boolean isCooldownPassed(long now, long lastWarm) {
        return now - lastWarm >= COOLDOWN.toMillis();
    }
}
