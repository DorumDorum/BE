package com.project.dorumdorum.domain.chat.presence.state;

import com.project.dorumdorum.domain.chat.presence.NotificationChannel;

public interface PresenceState {
    NotificationChannel decideMessageChannel(String roomId);
    NotificationChannel decideRequestChannel(String roomId);
}
