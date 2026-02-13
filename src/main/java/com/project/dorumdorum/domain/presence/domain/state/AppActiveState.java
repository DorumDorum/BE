package com.project.dorumdorum.domain.presence.domain.state;

import com.project.dorumdorum.domain.notification.domain.NotificationChannel;

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
