package com.project.dorumdorum.domain.notification.infra.sse;

import com.project.dorumdorum.domain.notification.domain.service.UserPresenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * SSE 연결 유저의 presence TTL을 갱신하고 하트비트 이벤트 전송.
 * 30초마다 실행 (TTL 60초보다 짧게).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {

    private static final int HEARTBEAT_INTERVAL_MS = 30_000;

    private final SseEmitterRegistry sseEmitterRegistry;
    private final UserPresenceRepository userPresenceRepository;

    @Scheduled(fixedRate = HEARTBEAT_INTERVAL_MS)
    public void run() {
        for (String userNo : sseEmitterRegistry.getConnectedUserNos()) {
            try {
                userPresenceRepository.setOnline(userNo);
                sseEmitterRegistry.sendHeartbeatToUser(userNo);
            } catch (Exception e) {
                log.warn("[SSE] heartbeat failed userNo={}", userNo, e);
            }
        }
    }
}
