package com.project.dorumdorum.domain.chat.application.aop;

import com.project.dorumdorum.domain.chat.application.event.ChatEventPublisher;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestCreatedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageRequestDecidedEvent;
import com.project.dorumdorum.domain.chat.application.event.MessageSentEvent;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ChatNotificationPublishAspect {

    private final ChatEventPublisher chatEventPublisher;

    @AfterReturning(pointcut = "@annotation(notificationPublish)", returning = "event")
    public void publish(NotificationPublish notificationPublish, Object event) {
        if (event == null) {
            throw new IllegalStateException("Notification event is null for subject " + notificationPublish.subject());
        }

        switch (notificationPublish.subject()) {
            case MESSAGE_SENT -> {
                if (!(event instanceof MessageSentEvent messageSentEvent)) {
                    throw new IllegalStateException("Expected MessageSentEvent but got " + event.getClass().getName());
                }
                chatEventPublisher.publishMessageSent(messageSentEvent);
            }
            case MESSAGE_REQUEST_CREATED -> {
                if (!(event instanceof MessageRequestCreatedEvent createdEvent)) {
                    throw new IllegalStateException("Expected MessageRequestCreatedEvent but got " + event.getClass().getName());
                }
                chatEventPublisher.publishMessageRequestCreated(createdEvent);
            }
            case MESSAGE_REQUEST_DECIDED -> {
                if (!(event instanceof MessageRequestDecidedEvent decidedEvent)) {
                    throw new IllegalStateException("Expected MessageRequestDecidedEvent but got " + event.getClass().getName());
                }
                chatEventPublisher.publishMessageRequestDecided(decidedEvent);
            }
        }
    }
}
