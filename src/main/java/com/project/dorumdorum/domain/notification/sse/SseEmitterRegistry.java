package com.project.dorumdorum.domain.notification.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseEmitterRegistry {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(String userId, SseEmitter emitter) {
        SseEmitter previous = emitters.put(userId, emitter);
        if (previous != null) {
            previous.complete();
        }
        return emitter;
    }

    public Optional<SseEmitter> get(String userId) {
        return Optional.ofNullable(emitters.get(userId));
    }

    public void remove(String userId) {
        emitters.remove(userId);
    }

    public void completeAndRemoveEmitter(String userId) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.complete();
            } catch (IllegalStateException e) {
                log.error("SseEmitter already completed: userId={}", userId, e);
            } finally {
                emitters.remove(userId);
            }
        }
    }

    public Map<String, SseEmitter> getEmitters() {
        return new ConcurrentHashMap<>(emitters);
    }
}
