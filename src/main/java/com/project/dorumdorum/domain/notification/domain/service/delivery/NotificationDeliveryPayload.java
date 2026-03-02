package com.project.dorumdorum.domain.notification.domain.service.delivery;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;

public record NotificationDeliveryPayload(
        String notificationNo,
        String recipientNo,
        String title,
        String body,
        NotificationType type,
        String relatedId,
        String redirectPath
) {
}
