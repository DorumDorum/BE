package com.project.dorumdorum.domain.presence.domain.state;

import com.project.dorumdorum.domain.notification.domain.NotificationChannel;

public interface PresenceState {
    NotificationChannel decideMessageChannel(String roomId);
    NotificationChannel decideRequestChannel(String roomId);
}
