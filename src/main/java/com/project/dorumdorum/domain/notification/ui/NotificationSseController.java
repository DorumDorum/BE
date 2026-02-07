package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.chat.presence.PresenceService;
import com.project.dorumdorum.domain.chat.presence.PresenceSnapshot;
import com.project.dorumdorum.domain.chat.presence.PresenceStatus;
import com.project.dorumdorum.domain.notification.sse.SseEmitterRegistry;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class NotificationSseController {

    private final SseEmitterRegistry emitterRegistry;
    private final PresenceService presenceService;

    @Value("${notification.sse.timeout-ms:1800000}")
    private long timeoutMs;

    @GetMapping(value = "/api/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@CurrentUser Long userId) throws IOException {
        SseEmitter emitter = new SseEmitter(timeoutMs);
        emitterRegistry.register(userId, emitter);

        PresenceSnapshot current = presenceService.getPresence(userId);
        if (current.status() != PresenceStatus.IN_ROOM) {
            presenceService.setAppActive(userId);
        }

        emitter.onCompletion(() -> onDisconnect(userId));
        emitter.onTimeout(() -> onDisconnect(userId));
        emitter.onError(e -> onDisconnect(userId));

        emitter.send(SseEmitter.event().name("connected").data("ok"));
        return emitter;
    }

    private void onDisconnect(Long userId) {
        emitterRegistry.remove(userId);
        PresenceSnapshot current = presenceService.getPresence(userId);
        if (current.status() == PresenceStatus.APP_ACTIVE) {
            presenceService.setAppInactive(userId);
        }
    }
}
