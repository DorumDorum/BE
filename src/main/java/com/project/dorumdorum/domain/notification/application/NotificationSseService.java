package com.project.dorumdorum.domain.notification.application;

import com.project.dorumdorum.domain.chat.presence.PresenceService;
import com.project.dorumdorum.domain.chat.presence.PresenceSnapshot;
import com.project.dorumdorum.domain.chat.presence.PresenceStatus;
import com.project.dorumdorum.domain.notification.sse.SseEmitterRegistry;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import com.project.dorumdorum.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationSseService {

    private final SseEmitterRegistry emitterRegistry;
    private final PresenceService presenceService;
    private final TokenProvider tokenProvider;

    public SseEmitter connect(String accessToken, long timeoutMs) throws IOException {
        String userId = authenticate(accessToken);

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

    private String authenticate(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }

        if (!tokenProvider.validateToken(accessToken) || !tokenProvider.isAccessToken(accessToken)) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }

        return tokenProvider.getId(accessToken)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._UNAUTHORIZED));
    }

    private void onDisconnect(String userId) {
        emitterRegistry.remove(userId);
        PresenceSnapshot current = presenceService.getPresence(userId);
        if (current.status() == PresenceStatus.APP_ACTIVE) {
            presenceService.setAppInactive(userId);
        }
    }
}
