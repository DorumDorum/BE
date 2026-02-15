package com.project.dorumdorum.global.logging;

public record RequestLogContext(
        String traceId,
        String method,
        String uri,
        String userNo,
        String ip,
        int status
) {
}
