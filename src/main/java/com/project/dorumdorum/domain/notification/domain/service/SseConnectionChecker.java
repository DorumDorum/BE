package com.project.dorumdorum.domain.notification.domain.service;

public interface SseConnectionChecker {

    boolean hasConnection(String userNo, String deviceId);
}
