package com.project.dorumdorum.global.logging;

import com.project.dorumdorum.global.properties.LoggingPolicyProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.entries;

@Slf4j
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_KEY = "traceId";
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    private final RequestLogContextResolver requestLogContextResolver;
    private final StructuredLogFactory structuredLogFactory;
    private final LoggingPolicyProperties loggingPolicyProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String previousTraceId = MDC.get(TRACE_ID_KEY);
        String traceId = resolveTraceId(request);
        long start = System.currentTimeMillis();

        MDC.put(TRACE_ID_KEY, traceId);
        request.setAttribute(TRACE_ID_KEY, traceId);
        response.setHeader(TRACE_ID_HEADER, traceId);
        RequestLogContext startContext = requestLogContextResolver.resolve(request, response, 200);
        log.info("요청 수신 {}", entries(structuredLogFactory.requestStarted(startContext)));

        try {
            filterChain.doFilter(request, response);
        } finally {
            long elapsedMs = System.currentTimeMillis() - start;
            RequestLogContext endContext = requestLogContextResolver.resolve(request, response, 200);
            Map<String, Object> endLog = structuredLogFactory.requestFinished(endContext, elapsedMs);
            if (endContext.status() >= 500) {
                log.error("요청 종료 {}", entries(endLog));
            } else if (elapsedMs >= loggingPolicyProperties.slowResponseThresholdMs()) {
                log.warn("요청 종료 {}", entries(endLog));
            } else {
                log.info("요청 종료 {}", entries(endLog));
            }

            if (previousTraceId == null || previousTraceId.isBlank()) {
                MDC.remove(TRACE_ID_KEY);
            } else {
                MDC.put(TRACE_ID_KEY, previousTraceId);
            }
        }
    }

    private String resolveTraceId(HttpServletRequest request) {
        String headerTraceId = request.getHeader(TRACE_ID_HEADER);
        if (headerTraceId != null && !headerTraceId.isBlank()) {
            return headerTraceId;
        }
        return UUID.randomUUID().toString();
    }
}
