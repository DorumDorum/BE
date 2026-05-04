package com.project.dorumdorum.domain.chat.application.event;

import java.util.List;

public record ChatWebSocketNotificationEvent(
        List<BroadcastTask> broadcasts,
        List<UserNotifyTask> userNotifications
) {
    public record BroadcastTask(String chatRoomNo, Object payload) {}

    public record UserNotifyTask(String userNo, Object payload) {}
}
