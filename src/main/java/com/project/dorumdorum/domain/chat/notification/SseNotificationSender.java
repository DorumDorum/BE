package com.project.dorumdorum.domain.chat.notification;

import com.project.dorumdorum.domain.chat.application.event.MessageRequestCreatedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestDecidedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageSentEvent;
import com.project.dorumdorum.domain.notification.sse.SseEmitterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseNotificationSender {

    private final SseEmitterRegistry emitterRegistry;

    public boolean sendMessage(Long userId, MessageSentEvent event) {
        return send(userId, "chat.message", event);
    }

    public boolean sendRequestCreated(Long userId, MessageRequestCreatedEvent event) {
        return send(userId, "chat.request.created", event);
    }

    public boolean sendRequestDecided(Long userId, MessageRequestDecidedEvent event) {
        return send(userId, "chat.request.decided", event);
    }

    private boolean send(Long userId, String eventName, Object payload) {
        SseEmitter emitter = emitterRegistry.get(userId).orElse(null);
        if (emitter == null) {
            return false;
        }
        try {
            emitter.send(SseEmitter.event().name(eventName).data(payload));
            return true;
        } catch (Exception e) {
            log.warn("[SSE] send failed. userId={} event={}", userId, eventName, e);
            emitterRegistry.remove(userId);
            return false;
        }
    }
}
