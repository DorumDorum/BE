package com.project.dorumdorum.global.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.entries;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventLogger {

    private final StructuredLogFactory structuredLogFactory;

    public void info(String event, String action, Map<String, Object> payload) {
        Map<String, Object> data = withTraceId(structuredLogFactory.domainEvent(event, action, payload));
        log.info("도메인 이벤트 {}", entries(data));
    }

    public void warn(String event, String action, Map<String, Object> payload, Throwable throwable) {
        Map<String, Object> data = withTraceId(structuredLogFactory.domainEvent(event, action, payload));
        log.warn("도메인 이벤트 실패 {}", entries(data), throwable);
    }

    public void error(String event, String action, Map<String, Object> payload, Throwable throwable) {
        Map<String, Object> data = withTraceId(structuredLogFactory.domainEvent(event, action, payload));
        log.error("도메인 이벤트 오류 {}", entries(data), throwable);
    }

    private Map<String, Object> withTraceId(Map<String, Object> payload) {
        Map<String, Object> map = new LinkedHashMap<>(payload);
        map.put("traceId", MDC.get(RequestLoggingFilter.TRACE_ID_KEY));
        return Map.copyOf(map);
    }
}
