package com.project.dorumdorum.global.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SystemAlertPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(SystemAlert alert) {
        eventPublisher.publishEvent(new SystemAlertEvent(alert));
    }

    public void publish(
            AlertSeverity severity,
            AlertType type,
            String title,
            String message,
            Map<String, Object> data
    ) {
        publish(new SystemAlert(severity, type, title, message, data));
    }

    public void publish(
            AlertSeverity severity,
            AlertType type,
            String title,
            String message
    ) {
        publish(severity, type, title, message, Map.of());
    }
}

