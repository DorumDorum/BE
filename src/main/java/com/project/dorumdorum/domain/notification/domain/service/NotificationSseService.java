package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.presence.domain.service.PresenceService;
import com.project.dorumdorum.domain.notification.sse.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSseService {

    private final SseEmitterRegistry emitterRegistry;
    private final PresenceService presenceService;

    public SseEmitter connect(String userId, long timeoutMs) throws IOException {
        SseEmitter emitter = new SseEmitter(timeoutMs);
        emitterRegistry.register(userId, emitter);

        presenceService.onSseConnect(userId);

        emitter.onCompletion(() -> {
            log.info("SseEmitter completed: userId={}", userId);
            onDisconnect(userId);
        });
        emitter.onTimeout(() -> {
            log.info("SseEmitter timeout: userId={}", userId);
            onDisconnect(userId);
        });
        emitter.onError(e -> {
            log.error("SseEmitter error: userId={}", userId, e);
            onDisconnect(userId);
        });

        emitter.send(SseEmitter.event().name("connected").data("ok"));
        return emitter;
    }

    private void onDisconnect(String userId) {
        emitterRegistry.remove(userId);
        presenceService.onSseDisconnect(userId);
    }
}
