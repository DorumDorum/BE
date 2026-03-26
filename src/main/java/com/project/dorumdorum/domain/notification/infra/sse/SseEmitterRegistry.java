package com.project.dorumdorum.domain.notification.infra.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dorumdorum.domain.notification.domain.repository.UserPresenceRepository;
import com.project.dorumdorum.domain.notification.domain.service.delivery.NotificationDeliveryPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitterRegistry {

    private static final long SSE_TIMEOUT_MS = 60 * 60 * 1000L; // 1시간

    private final ObjectMapper objectMapper;
    private final UserPresenceRepository userPresenceRepository;

    private final Map<String, Map<String, SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    public SseEmitter register(String userNo, String deviceId) {
        String key = deviceId;
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);

        Map<String, SseEmitter> emitters = userEmitters.computeIfAbsent(userNo, k -> new ConcurrentHashMap<>());
        SseEmitter previous = emitters.put(key, emitter);
        if (previous != null) {
            try {
                previous.complete();
            } catch (Exception ignored) {}
        }

        if (emitters.size() == 1)
            userPresenceRepository.setOnline(userNo);

        // remove() -> removeIfCurrent()
        emitter.onCompletion(() -> removeIfCurrent(userNo, key, emitter));
        emitter.onTimeout(() -> removeIfCurrent(userNo, key, emitter));
        emitter.onError(e -> removeIfCurrent(userNo, key, emitter));

        return emitter;
    }

    public void remove(String userNo, String deviceId) {
        Map<String, SseEmitter> emitters = userEmitters.get(userNo);
        if (emitters != null) {
            emitters.remove(deviceId);
            if (emitters.isEmpty()) {
                userEmitters.remove(userNo);
                userPresenceRepository.setOffline(userNo);
            }
        }
    }

    private void removeIfCurrent(String userNo, String deviceId, SseEmitter target) {
        Map<String, SseEmitter> emitters = userEmitters.get(userNo);
        if (emitters != null && emitters.get(deviceId) == target) {
            emitters.remove(deviceId);
            if (emitters.isEmpty()) {
                userEmitters.remove(userNo);
                userPresenceRepository.setOffline(userNo);
            }
        }
    }

    public boolean hasConnection(String userNo, String deviceId) {
        if (deviceId == null || deviceId.isBlank())
            return false;
        Map<String, SseEmitter> emitters = userEmitters.get(userNo);
        if (emitters == null || emitters.isEmpty())
            return false;
        return emitters.containsKey(deviceId);
    }

    public boolean hasConnection(String userNo) {
        Map<String, SseEmitter> emitters = userEmitters.get(userNo);
        return emitters != null && !emitters.isEmpty();
    }

    public Set<String> getConnectedUsers() {
        Set<String> result = new java.util.HashSet<>();
        userEmitters.forEach((userNo, map) -> {
            if (map != null && !map.isEmpty())
                result.add(userNo);
        });
        return Collections.unmodifiableSet(result);
    }

    public void sendHeartbeatToUser(String userNo) {
        Map<String, SseEmitter> emitters = userEmitters.get(userNo);
        if (emitters == null || emitters.isEmpty())
            return;

        emitters.entrySet().removeIf(entry -> {
            try {
                entry.getValue().send(SseEmitter.event().name("heartbeat").data("{}"));
                return false;
            } catch (Exception e) {
                log.debug("[SSE] heartbeat send failed userNo={} deviceId={}: {}", userNo, entry.getKey(), e.getMessage());
                return true;
            }
        });
        if (emitters.isEmpty()) {
            userEmitters.remove(userNo);
            userPresenceRepository.setOffline(userNo);
        }
    }

    public void sendToDevice(String userNo, String deviceId, NotificationDeliveryPayload payload) {
        if (deviceId == null || deviceId.isBlank())
            return;
        Map<String, SseEmitter> emitters = userEmitters.get(userNo);
        if (emitters == null || emitters.isEmpty())
            return;

        SseEmitter emitter = emitters.get(deviceId);
        if (emitter == null)
            return;

        String json = toJson(payload);
        if (json == null)
            return;

        try {
            emitter.send(SseEmitter.event().data(json));
        } catch (Exception e) {
            log.warn("[SSE] send failed userNo={} deviceId={}: {}", userNo, deviceId, e.getMessage());
            emitters.remove(deviceId);
            if (emitters.isEmpty()) {
                userEmitters.remove(userNo);
                userPresenceRepository.setOffline(userNo);
            }
        }
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
