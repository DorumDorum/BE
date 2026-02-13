package com.project.dorumdorum.domain.presence.domain.state;

import com.project.dorumdorum.domain.notification.domain.NotificationChannel;

public class InRoomState implements PresenceState {

    private final String currentRoomId;

    public InRoomState(String currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    @Override
    public NotificationChannel decideMessageChannel(String roomId) {
        if (currentRoomId != null && currentRoomId.equals(roomId)) {
            return NotificationChannel.STOMP;
        }
        return NotificationChannel.SSE;
    }

    @Override
    public NotificationChannel decideRequestChannel(String roomId) {
        return decideMessageChannel(roomId);
    }
}
