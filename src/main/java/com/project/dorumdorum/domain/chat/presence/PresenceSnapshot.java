package com.project.dorumdorum.domain.chat.presence;

import java.time.LocalDateTime;

public record PresenceSnapshot(
    String userId,
    PresenceStatus status,
    String roomId,
    LocalDateTime updatedAt
) {
    public static PresenceSnapshot inRoom(String userId, String roomId) {
        return new PresenceSnapshot(userId, PresenceStatus.IN_ROOM, roomId, LocalDateTime.now());
    }

    public static PresenceSnapshot appActive(String userId) {
        return new PresenceSnapshot(userId, PresenceStatus.APP_ACTIVE, null, LocalDateTime.now());
    }

    public static PresenceSnapshot appInactive(String userId) {
        return new PresenceSnapshot(userId, PresenceStatus.APP_INACTIVE, null, LocalDateTime.now());
    }
}
