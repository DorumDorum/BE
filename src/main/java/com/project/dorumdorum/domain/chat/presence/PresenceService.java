package com.project.dorumdorum.domain.chat.presence;

import com.project.dorumdorum.domain.chat.presence.state.AppActiveState;
import com.project.dorumdorum.domain.chat.presence.state.AppInactiveState;
import com.project.dorumdorum.domain.chat.presence.state.InRoomState;
import com.project.dorumdorum.domain.chat.presence.state.PresenceState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService {

    private final PresenceRepository presenceRepository;

    @Value("${presence.ttl-seconds:300}")
    private long ttlSeconds;

    public void setInRoom(String userId, String roomId) {
        log.info("[Presence] IN_ROOM userId={} roomId={}", userId, roomId);
        presenceRepository.save(PresenceSnapshot.inRoom(userId, roomId), ttlSeconds);
    }

    public void setAppActive(String userId) {
        log.info("[Presence] APP_ACTIVE userId={}", userId);
        presenceRepository.save(PresenceSnapshot.appActive(userId), ttlSeconds);
    }

    public void setAppInactive(String userId) {
        log.info("[Presence] APP_INACTIVE userId={}", userId);
        presenceRepository.save(PresenceSnapshot.appInactive(userId), ttlSeconds);
    }

    public PresenceSnapshot getPresence(String userId) {
        PresenceSnapshot snapshot = presenceRepository.find(userId)
            .orElseGet(() -> PresenceSnapshot.appInactive(userId));
        log.info("[PresenceService] getPresence userId={} status={} roomId={}", userId, snapshot.status(), snapshot.roomId());
        return snapshot;
    }

    public NotificationChannel decideMessageChannel(String userId, String roomId) {
        PresenceSnapshot snapshot = getPresence(userId);
        PresenceState state = toState(snapshot);
        return state.decideMessageChannel(roomId);
    }

    public NotificationChannel decideRequestChannel(String userId, String roomId) {
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
