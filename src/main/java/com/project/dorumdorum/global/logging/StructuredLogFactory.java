package com.project.dorumdorum.global.logging;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class StructuredLogFactory {

    public Map<String, Object> requestStarted(RequestLogContext context) {
        return base(LogCategory.REQUEST, LogEvent.REQUEST_RECEIVED.name(), context).build();
    }

    public Map<String, Object> requestFinished(RequestLogContext context, long elapsedMs) {
        return base(LogCategory.RESPONSE, LogEvent.REQUEST_COMPLETED.name(), context)
                .append("elapsedMs", elapsedMs)
                .build();
    }

    public Map<String, Object> requestReceived(RequestLogContext context, String args) {
        return base(LogCategory.REQUEST, LogEvent.REQUEST_RECEIVED.name(), context)
                .append("args", args)
                .build();
    }

    public Map<String, Object> requestCompleted(RequestLogContext context, long elapsedMs, int responseSize) {
        return base(LogCategory.RESPONSE, LogEvent.REQUEST_COMPLETED.name(), context)
                .append("elapsedMs", elapsedMs)
                .append("responseSize", responseSize)
                .build();
    }

    public Map<String, Object> requestFailed(RequestLogContext context, Throwable exception, long elapsedMs, String message) {
        return base(LogCategory.ERROR, LogEvent.REQUEST_FAILED.name(), context)
                .append("elapsedMs", elapsedMs)
                .append("errorType", exception.getClass().getSimpleName())
                .append("errorMessage", message)
                .build();
    }

    public Map<String, Object> domainEvent(String event, String action, Map<String, Object> payload) {
        LogMapBuilder builder = new LogMapBuilder()
                .append("category", LogCategory.DOMAIN_EVENT.name())
                .append("event", event)
                .append("action", action);
        if (payload != null && !payload.isEmpty()) {
            payload.forEach(builder::append);
        }
        return builder.build();
    }

    private LogMapBuilder base(LogCategory category, String event, RequestLogContext context) {
        return new LogMapBuilder()
                .append("category", category.name())
                .append("event", event)
                .append("traceId", context.traceId())
                .append("method", context.method())
                .append("uri", context.uri())
                .append("status", context.status())
                .append("userNo", context.userNo())
                .append("ip", context.ip());
    }

    private static final class LogMapBuilder {
        private final Map<String, Object> values = new LinkedHashMap<>();

        private LogMapBuilder append(String key, Object value) {
            values.put(key, value);
            return this;
        }

        private Map<String, Object> build() {
            return Map.copyOf(values);
        }
    }
}
