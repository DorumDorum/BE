package com.project.dorumdorum.global.logging;

import com.project.dorumdorum.global.properties.LoggingMaskingProperties;
import com.project.dorumdorum.global.properties.LoggingPolicyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LogRedactor {

    private static final String MASKED = "[MASKED]";

    private final LoggingMaskingProperties loggingMaskingProperties;
    private final LoggingPolicyProperties loggingPolicyProperties;

    public String redactArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        return Arrays.stream(args)
                .map(arg -> redactAndLimit(arg, loggingPolicyProperties.maxArgLength()))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public String redactResult(Object result) {
        if (result == null) {
            return "void";
        }
        return redactAndLimit(result, loggingPolicyProperties.maxResultLength());
    }

    public String redactText(String text) {
        if (text == null) {
            return "";
        }
        if (containsSensitiveData(text)) {
            return MASKED;
        }
        return text;
    }

    private String redactAndLimit(Object value, int maxLength) {
        if (value == null) {
            return "null";
        }
        String text = value.toString();
        if (containsSensitiveData(text)) {
            return MASKED;
        }
        if (text.length() > maxLength) {
            return text.substring(0, maxLength) + "...";
        }
        return text;
    }

    private boolean containsSensitiveData(String value) {
        String lower = value.toLowerCase(Locale.ROOT);
        return loggingMaskingProperties.sensitivePatterns().stream()
                .map(pattern -> pattern.toLowerCase(Locale.ROOT))
                .anyMatch(lower::contains);
    }
}
