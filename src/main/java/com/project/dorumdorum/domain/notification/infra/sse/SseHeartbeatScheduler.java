package com.project.dorumdorum.domain.notification.infra.sse;

import com.project.dorumdorum.domain.notification.domain.repository.UserPresenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {

    private static final int HEARTBEAT_INTERVAL_MS = 30_000;    // 30초

    private final SseEmitterRegistry sseEmitterRegistry;
    private final UserPresenceRepository userPresenceRepository;

    @Scheduled(fixedRate = HEARTBEAT_INTERVAL_MS)
    public void run() {
        for (String userNo : sseEmitterRegistry.getConnectedUsers()) {
            try {
                userPresenceRepository.setOnline(userNo);
                sseEmitterRegistry.sendHeartbeatToUser(userNo);
            } catch (Exception e) {
                log.warn("[SSE] heartbeat failed userNo={}", userNo, e);
            }
        }
    }
}
