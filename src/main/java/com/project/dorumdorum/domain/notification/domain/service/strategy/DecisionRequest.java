package com.project.dorumdorum.domain.notification.domain.service.strategy;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;

public record DecisionRequest(
        NotificationType type,
        String relatedId,
        UserPresence presence
) {}
