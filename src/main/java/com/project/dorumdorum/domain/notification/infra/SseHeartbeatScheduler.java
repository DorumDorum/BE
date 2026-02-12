package com.project.dorumdorum.domain.notification.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Map;
import com.project.dorumdorum.domain.notification.sse.SseEmitterRegistry;
import com.project.dorumdorum.domain.chat.presence.PresenceService;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseHeartbeatScheduler {

    private final SseEmitterRegistry emitterRegistry;
    private final PresenceService presenceService;

    @Scheduled(fixedDelayString = "${notification.sse.heartbeat-interval-ms:30000}")
    public void sendHeartbeat() {
        
        Map<String, SseEmitter> emitters = emitterRegistry.getEmitters();

        if(emitters == null || emitters.isEmpty()) {
            return;
        }

        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
            String userId = entry.getKey();
            SseEmitter emitter = entry.getValue();

            try {
                log.info("SseEmitter ping: userId={}", userId);
                emitter.send(SseEmitter.event().name("heartbeat").data("ping"));
                presenceService.onSseHeartbeat(userId);
            } catch (IllegalStateException e) {
                emitterRegistry.completeAndRemoveEmitter(userId);
                log.error("SseEmitter already completed: userId={}", userId, e);
                presenceService.onSseDisconnect(userId);
            } catch (IOException e) {
                emitterRegistry.completeAndRemoveEmitter(userId);
                log.error("Disconnected SseEmitter: userId={}", userId, e);
                presenceService.onSseDisconnect(userId);
            }
        }
    }
}
