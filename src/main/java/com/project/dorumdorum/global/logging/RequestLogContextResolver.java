package com.project.dorumdorum.global.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RequestLogContextResolver {

    public RequestLogContext resolve(HttpServletRequest request, HttpServletResponse response, int fallbackStatus) {
        String traceId = resolveTraceId(request);
        String method = request != null ? request.getMethod() : "N/A";
        String uri = request != null ? request.getRequestURI() : "N/A";
        String userNo = resolveUserNo();
        String ip = request != null ? resolveClientIp(request) : "N/A";
        int status = resolveStatus(response, fallbackStatus);
        return new RequestLogContext(traceId, method, uri, userNo, ip, status);
    }

    private String resolveTraceId(HttpServletRequest request) {
        if (request == null) {
            return "N/A";
        }
        Object traceId = request.getAttribute(RequestLoggingFilter.TRACE_ID_KEY);
        if (traceId == null) {
            return "N/A";
        }
        String traceIdString = traceId.toString();
        return traceIdString.isBlank() ? "N/A" : traceIdString;
    }

    private String resolveUserNo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }
        String name = authentication.getName();
        if (name == null || name.isBlank() || "anonymousUser".equalsIgnoreCase(name)) {
            return "anonymous";
        }
        return name;
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private int resolveStatus(HttpServletResponse response, int fallbackStatus) {
        if (response == null) {
            return fallbackStatus;
        }
        int status = response.getStatus();
        return status > 0 ? status : fallbackStatus;
    }
}
