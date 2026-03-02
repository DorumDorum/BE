package com.project.dorumdorum.domain.notification.infra.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseConnectionCheckerImpl implements SseConnectionChecker {

    private final SseEmitterRegistry sseEmitterRegistry;

    @Override
    public boolean hasConnection(String userNo, String deviceId) {
        return sseEmitterRegistry.hasConnection(userNo, deviceId);
    }
}
