package com.project.dorumdorum.domain.chat.presence;

import java.time.LocalDateTime;

public record PresenceSnapshot(
    Long userId,
    PresenceStatus status,
    Long roomId,
    LocalDateTime updatedAt
) {
    public static PresenceSnapshot inRoom(Long userId, Long roomId) {
        return new PresenceSnapshot(userId, PresenceStatus.IN_ROOM, roomId, LocalDateTime.now());
    }

    public static PresenceSnapshot appActive(Long userId) {
        return new PresenceSnapshot(userId, PresenceStatus.APP_ACTIVE, null, LocalDateTime.now());
    }

    public static PresenceSnapshot appInactive(Long userId) {
        return new PresenceSnapshot(userId, PresenceStatus.APP_INACTIVE, null, LocalDateTime.now());
    }
}
