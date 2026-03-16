package com.project.dorumdorum.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "notification.rate-limit.fcm")
public class NotificationRateLimitProperties {

    private boolean enabled = true;
    private String key = "notification:fcm:rate-limit";
    private long permitsPerSecond = 30;
    private long bucketCapacity = 120;
}
