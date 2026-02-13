package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.presence.domain.service.PresenceService;
import com.project.dorumdorum.domain.notification.sse.SseEmitterRegistry;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import com.project.dorumdorum.global.security.TokenProvider;
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
    private final TokenProvider tokenProvider;

    public SseEmitter connect(String accessToken, long timeoutMs) throws IOException {
        String userId = authenticate(accessToken);

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
        presenceService.onSseDisconnect(userId);
    }
}
