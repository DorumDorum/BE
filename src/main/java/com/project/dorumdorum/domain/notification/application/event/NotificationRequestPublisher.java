package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRequestPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(String recipientNo, String title, String body, NotificationType type, String relatedId) {
        eventPublisher.publishEvent(
                new NotificationRequestEvent(recipientNo, title, body, type, relatedId)
        );
    }

    public void publish(String recipientNo, String title, String body, NotificationType type) {
        publish(recipientNo, title, body, type, null);
    }
}
