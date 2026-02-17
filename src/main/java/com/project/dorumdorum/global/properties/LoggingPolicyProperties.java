package com.project.dorumdorum.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logging.policy")
public record LoggingPolicyProperties(
        int maxArgLength,
        int maxResultLength,
        boolean includeStackTrace,
        long slowResponseThresholdMs
) {
    public LoggingPolicyProperties {
        if (maxArgLength <= 0) {
            maxArgLength = 200;
        }
        if (maxResultLength <= 0) {
            maxResultLength = 300;
        }
        if (slowResponseThresholdMs <= 0) {
            slowResponseThresholdMs = 1000;
        }
    }
}
