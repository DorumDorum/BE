package com.project.dorumdorum.domain.notification.infra.sse;

public interface SseConnectionChecker {

    boolean hasConnection(String userNo, String deviceId);
}
