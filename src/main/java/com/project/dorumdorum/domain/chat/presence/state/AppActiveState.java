package com.project.dorumdorum.domain.chat.presence.state;

import com.project.dorumdorum.domain.chat.presence.NotificationChannel;

public class AppActiveState implements PresenceState {
    @Override
    public NotificationChannel decideMessageChannel(String roomId) {
        return NotificationChannel.SSE;
    }

    @Override
    public NotificationChannel decideRequestChannel(String roomId) {
        return NotificationChannel.SSE;
    }
}
