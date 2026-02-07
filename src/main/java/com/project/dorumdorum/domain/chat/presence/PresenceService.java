package com.project.dorumdorum.domain.chat.presence;

import com.project.dorumdorum.domain.chat.presence.state.AppActiveState;
import com.project.dorumdorum.domain.chat.presence.state.AppInactiveState;
import com.project.dorumdorum.domain.chat.presence.state.InRoomState;
import com.project.dorumdorum.domain.chat.presence.state.PresenceState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresenceService {

    private final PresenceRepository presenceRepository;

    @Value("${presence.ttl-seconds:300}")
    private long ttlSeconds;

    public void setInRoom(Long userId, Long roomId) {
        presenceRepository.save(PresenceSnapshot.inRoom(userId, roomId), ttlSeconds);
    }

    public void setAppActive(Long userId) {
        presenceRepository.save(PresenceSnapshot.appActive(userId), ttlSeconds);
    }

    public void setAppInactive(Long userId) {
        presenceRepository.save(PresenceSnapshot.appInactive(userId), ttlSeconds);
    }

    public PresenceSnapshot getPresence(Long userId) {
        return presenceRepository.find(userId)
            .orElseGet(() -> PresenceSnapshot.appInactive(userId));
    }

    public NotificationChannel decideMessageChannel(Long userId, Long roomId) {
        PresenceSnapshot snapshot = getPresence(userId);
        PresenceState state = toState(snapshot);
        return state.decideMessageChannel(roomId);
    }

    public NotificationChannel decideRequestChannel(Long userId, Long roomId) {
        PresenceSnapshot snapshot = getPresence(userId);
        PresenceState state = toState(snapshot);
        return state.decideRequestChannel(roomId);
    }

    private PresenceState toState(PresenceSnapshot snapshot) {
        return switch (snapshot.status()) {
            case IN_ROOM -> new InRoomState(snapshot.roomId());
            case APP_ACTIVE -> new AppActiveState();
            case APP_INACTIVE -> new AppInactiveState();
        };
    }
}
