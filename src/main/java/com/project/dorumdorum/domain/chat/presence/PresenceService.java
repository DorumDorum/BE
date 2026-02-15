package com.project.dorumdorum.domain.chat.presence;

import com.project.dorumdorum.domain.chat.presence.state.AppActiveState;
import com.project.dorumdorum.domain.chat.presence.state.AppInactiveState;
import com.project.dorumdorum.domain.chat.presence.state.InRoomState;
import com.project.dorumdorum.domain.chat.presence.state.PresenceState;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PresenceService {

    private final PresenceRepository presenceRepository;
    private final DomainEventLogger domainEventLogger;

    @Value("${presence.ttl-seconds:300}")
    private long ttlSeconds;

    public void setInRoom(String userId, String roomId) {
        domainEventLogger.info("presence", "IN_ROOM", Map.of("userNo", userId, "roomId", roomId));
        presenceRepository.save(PresenceSnapshot.inRoom(userId, roomId), ttlSeconds);
    }

    public void setAppActive(String userId) {
        domainEventLogger.info("presence", "APP_ACTIVE", Map.of("userNo", userId));
        presenceRepository.save(PresenceSnapshot.appActive(userId), ttlSeconds);
    }

    public void setAppInactive(String userId) {
        domainEventLogger.info("presence", "APP_INACTIVE", Map.of("userNo", userId));
        presenceRepository.save(PresenceSnapshot.appInactive(userId), ttlSeconds);
    }

    public PresenceSnapshot getPresence(String userId) {
        PresenceSnapshot snapshot = presenceRepository.find(userId)
            .orElseGet(() -> PresenceSnapshot.appInactive(userId));
        domainEventLogger.info("presence", "GET_PRESENCE", Map.of(
                "userNo", userId,
                "status", snapshot.status().name(),
                "roomId", String.valueOf(snapshot.roomId())
        ));
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
