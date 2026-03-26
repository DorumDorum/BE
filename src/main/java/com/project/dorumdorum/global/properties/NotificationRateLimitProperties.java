package com.project.dorumdorum.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "notification.rate-limit.fcm")
public class NotificationRateLimitProperties {

    private boolean enabled;
    private String key;
    private long permitsPerWindow;
    private long windowMillis;
    private long ttlSeconds;
}
