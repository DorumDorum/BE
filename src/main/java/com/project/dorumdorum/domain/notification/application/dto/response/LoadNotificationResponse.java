package com.project.dorumdorum.domain.notification.application.dto.response;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadNotificationResponse(
        String notificationNo,
        String title,
        String body,
        NotificationType type,
        Boolean isRead,
        String relatedId,
        String redirectPath,
        LocalDateTime createdAt
) {
}
