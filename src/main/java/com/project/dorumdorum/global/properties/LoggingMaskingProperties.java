package com.project.dorumdorum.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "logging.masking")
public record LoggingMaskingProperties(List<String> sensitivePatterns) {

    public LoggingMaskingProperties {
        sensitivePatterns = sensitivePatterns == null ? List.of() : List.copyOf(sensitivePatterns);
    }
}
