package com.project.dorumdorum.domain.notification.application.helper;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;

public final class NotificationRedirectHelper {

    private NotificationRedirectHelper() {}

    public static String resolvePath(NotificationType type, String relatedId) {
        if (type == null) return null;
        String template = type.getPathTemplate();
        if (!template.contains("{")) return template;
        if (relatedId == null || relatedId.isBlank()) return null;
        return template.replaceAll("\\{\\w+\\}", relatedId);
    }
}
