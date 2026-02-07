package com.project.dorumdorum.domain.chat.presence.state;

import com.project.dorumdorum.domain.chat.presence.NotificationChannel;

public class AppActiveState implements PresenceState {
    @Override
    public NotificationChannel decideMessageChannel(Long roomId) {
        return NotificationChannel.SSE;
    }

    @Override
    public NotificationChannel decideRequestChannel(Long roomId) {
        return NotificationChannel.SSE;
    }
}
