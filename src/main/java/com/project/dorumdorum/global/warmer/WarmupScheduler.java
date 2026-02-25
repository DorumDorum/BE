package com.project.dorumdorum.global.warmer;

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
        warmupService.warm();
        lastWarmupTime.set(now);
    }

    private boolean isIdle(long now, long lastRequest) {
        return now - lastRequest >= IDLE_THRESHOLD.toMillis();
    }

    private boolean isCooldownPassed(long now, long lastWarm) {
        return now - lastWarm >= COOLDOWN.toMillis();
    }
}

