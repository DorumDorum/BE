package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.github.f4b6a3.tsid.TsidCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationRequestPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(String recipientNo, String title, String body, NotificationType type, String relatedId) {
        String outboxNo = TsidCreator.getTsid().toString();
        eventPublisher.publishEvent(
                new NotificationRequestEvent(outboxNo, recipientNo, title, body, type, relatedId)
        );
    }

    public void publish(String recipientNo, String title, String body, NotificationType type) {
        publish(recipientNo, title, body, type, null);
    }
}
