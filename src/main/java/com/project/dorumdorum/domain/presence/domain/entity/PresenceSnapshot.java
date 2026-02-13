package com.project.dorumdorum.domain.presence.domain.entity;

import java.time.LocalDateTime;

public record PresenceSnapshot(
    String userId,
    String roomId,
    boolean wsConnected,
    boolean sseConnected,
    LocalDateTime lastSeenAt
) {
    public static PresenceSnapshot initial(String userId) {
        return new PresenceSnapshot(userId, null, false, false, LocalDateTime.now());
    }

    public static PresenceSnapshot withRoom(String userId, String roomId, boolean wsConnected, boolean sseConnected) {
        return new PresenceSnapshot(userId, roomId, wsConnected, sseConnected, LocalDateTime.now());
    }

    public static PresenceSnapshot withFlags(String userId, boolean wsConnected, boolean sseConnected, String roomId, LocalDateTime lastSeenAt) {
        return new PresenceSnapshot(userId, roomId, wsConnected, sseConnected, lastSeenAt);
    }
}
