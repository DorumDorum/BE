package com.project.dorumdorum.domain.notification.infra.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitterRegistry {

    private static final long SSE_TIMEOUT_MS = 60 * 60 * 1000L; // 1시간

    private final ObjectMapper objectMapper;

    private final Map<String, Map<String, SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    public SseEmitter register(String userNo) {
        String emitterId = java.util.UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);

        userEmitters
                .computeIfAbsent(userNo, k -> new ConcurrentHashMap<>())
                .put(emitterId, emitter);

        emitter.onCompletion(() -> remove(userNo, emitterId));
        emitter.onTimeout(() -> remove(userNo, emitterId));
        emitter.onError(e -> remove(userNo, emitterId));

        return emitter;
    }

    public void remove(String userNo, String emitterId) {
        Map<String, SseEmitter> emitters = userEmitters.get(userNo);
        if (emitters != null) {
            emitters.remove(emitterId);
            if (emitters.isEmpty())
                userEmitters.remove(userNo);
        }
    }

    public void sendToUser(String userNo, NotificationDeliveryPayload payload) {
        Map<String, SseEmitter> emitters = userEmitters.get(userNo);
        if (emitters == null || emitters.isEmpty())
            return;

        String json = toJson(payload);
        if (json == null)
            return;

        emitters.entrySet().removeIf(entry -> {
            try {
                entry.getValue().send(SseEmitter.event().data(json));
                return false;
            } catch (IOException e) {
                log.warn("[SSE] send failed userNo={} emitterId={}", userNo, entry.getKey(), e);
                return true;
            }
        });
    }

    private String toJson(NotificationDeliveryPayload payload) {
        try {
            return objectMapper.writeValueAsString(Map.of(
                    "notificationNo", payload.notificationNo(),
                    "title", payload.title(),
                    "body", payload.body(),
                    "type", payload.type().name(),
                    "relatedId", payload.relatedId() != null ? payload.relatedId() : "",
                    "redirectPath", payload.redirectPath() != null ? payload.redirectPath() : ""
            ));
        } catch (JsonProcessingException e) {
            log.warn("[SSE] payload serialize failed", e);
            return null;
        }
    }
}
