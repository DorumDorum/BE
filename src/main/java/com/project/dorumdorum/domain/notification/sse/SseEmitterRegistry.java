package com.project.dorumdorum.domain.notification.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class SseEmitterRegistry {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(Long userId, SseEmitter emitter) {
        SseEmitter previous = emitters.put(userId, emitter);
        if (previous != null) {
            previous.complete();
        }
        return emitter;
    }

    public Optional<SseEmitter> get(Long userId) {
        return Optional.ofNullable(emitters.get(userId));
    }

    public void remove(Long userId) {
        emitters.remove(userId);
    }
}
