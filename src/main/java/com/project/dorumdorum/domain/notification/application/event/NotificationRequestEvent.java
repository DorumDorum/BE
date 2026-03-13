package com.project.dorumdorum.domain.notification.application.event;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;

public record NotificationRequestEvent(
        String outboxNo,
        String recipientNo,
        String title,
        String body,
        NotificationType type,
        String relatedId
) {
}
