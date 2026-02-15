package com.project.dorumdorum.domain.chat.notification;

import com.project.dorumdorum.domain.chat.application.event.MessageRequestCreatedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestDecidedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageSentEvent;
import com.project.dorumdorum.domain.notification.sse.SseEmitterRegistry;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SseNotificationSender {

    private final SseEmitterRegistry emitterRegistry;
    private final DomainEventLogger domainEventLogger;

    public boolean sendMessage(String userId, MessageSentEvent event) {
        return send(userId, "chat.message", event);
    }

    public boolean sendRequestCreated(String userId, MessageRequestCreatedEvent event) {
        return send(userId, "chat.request.created", event);
    }

    public boolean sendRequestDecided(String userId, MessageRequestDecidedEvent event) {
        return send(userId, "chat.request.decided", event);
    }

    private boolean send(String userId, String eventName, Object payload) {
        SseEmitter emitter = emitterRegistry.get(userId).orElse(null);
        if (emitter == null) {
            return false;
        }
        try {
            emitter.send(SseEmitter.event().name(eventName).data(payload));
            return true;
        } catch (Exception e) {
            domainEventLogger.warn("chat_notification", "SSE_SEND_FAILED", Map.of(
                    "userNo", userId,
                    "eventName", eventName
            ), e);
            emitterRegistry.remove(userId);
            return false;
        }
    }
}
