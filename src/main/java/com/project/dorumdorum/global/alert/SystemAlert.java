package com.project.dorumdorum.global.alert;

import java.util.Map;

public record SystemAlert(
        AlertSeverity severity,
        AlertType type,
        String title,
        String message,
        Map<String, Object> data
) {
    public static SystemAlert of(AlertSeverity severity, AlertType type, String title, String message) {
        return new SystemAlert(severity, type, title, message, Map.of());
    }
}

