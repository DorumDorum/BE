package com.project.dorumdorum.domain.chat.presence.state;

import com.project.dorumdorum.domain.chat.presence.NotificationChannel;

public class AppInactiveState implements PresenceState {
    @Override
    public NotificationChannel decideMessageChannel(Long roomId) {
        return NotificationChannel.FCM;
    }

    @Override
    public NotificationChannel decideRequestChannel(Long roomId) {
        return NotificationChannel.FCM;
    }
}
