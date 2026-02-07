package com.project.dorumdorum.domain.chat.presence.state;

import com.project.dorumdorum.domain.chat.presence.NotificationChannel;

public class InRoomState implements PresenceState {

    private final Long currentRoomId;

    public InRoomState(Long currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    @Override
    public NotificationChannel decideMessageChannel(Long roomId) {
        if (currentRoomId != null && currentRoomId.equals(roomId)) {
            return NotificationChannel.STOMP;
        }
        return NotificationChannel.SSE;
    }

    @Override
    public NotificationChannel decideRequestChannel(Long roomId) {
        return decideMessageChannel(roomId);
    }
}
