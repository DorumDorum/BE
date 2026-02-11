package com.project.dorumdorum.domain.chat.presence;

import com.project.dorumdorum.domain.chat.presence.state.AppActiveState;
import com.project.dorumdorum.domain.chat.presence.state.AppInactiveState;
import com.project.dorumdorum.domain.chat.presence.state.InRoomState;
import com.project.dorumdorum.domain.chat.presence.state.PresenceState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService {

    private final PresenceRepository presenceRepository;

    @Value("${presence.ttl-seconds:300}")
    private long ttlSeconds;

    public void onRoomsEnter(String userId, String roomId) {
        log.info("[Presence] ENTER userId={} roomId={}", userId, roomId);
        PresenceSnapshot current = getPresence(userId);
        PresenceSnapshot updated = PresenceSnapshot.withRoom(
            userId,
            roomId,
            true,
            current.sseConnected()
        );
        presenceRepository.save(updated, ttlSeconds);
    }

    public void onRoomsLeave(String userId) {
        log.info("[Presence] LEAVE userId={}", userId);
        PresenceSnapshot current = getPresence(userId);
        PresenceSnapshot updated = PresenceSnapshot.withFlags(
            userId,
            current.wsConnected(),
            current.sseConnected(),
            null,
            LocalDateTime.now()
        );
        presenceRepository.save(updated, ttlSeconds);
    }

    public void onWsConnect(String userId) {
        log.info("[Presence] WS_CONNECTED userId={}", userId);
        PresenceSnapshot current = getPresence(userId);
        PresenceSnapshot updated = PresenceSnapshot.withFlags(
            userId,
            true,
            current.sseConnected(),
            current.roomId(),
            LocalDateTime.now()
        );
        presenceRepository.save(updated, ttlSeconds);
    }

    public void onWsDisconnect(String userId) {
        log.info("[Presence] WS_DISCONNECTED userId={}", userId);
        PresenceSnapshot current = getPresence(userId);
        PresenceSnapshot updated = PresenceSnapshot.withFlags(
            userId,
            false,
            current.sseConnected(),
            null,
            LocalDateTime.now()
        );
        presenceRepository.save(updated, ttlSeconds);
    }

    public void onSseConnect(String userId) {
        log.info("[Presence] SSE_CONNECTED userId={}", userId);
        PresenceSnapshot current = getPresence(userId);
        PresenceSnapshot updated = PresenceSnapshot.withFlags(
            userId,
            current.wsConnected(),
            true,
            current.roomId(),
            LocalDateTime.now()
        );
        presenceRepository.save(updated, ttlSeconds);
    }

    public void onSseDisconnect(String userId) {
        log.info("[Presence] SSE_DISCONNECTED userId={}", userId);
        PresenceSnapshot current = getPresence(userId);
        PresenceSnapshot updated = PresenceSnapshot.withFlags(
            userId,
            current.wsConnected(),
            false,
            current.roomId(),
            LocalDateTime.now()
        );
        presenceRepository.save(updated, ttlSeconds);
    }

    public void onSseHeartbeat(String userId) {
        PresenceSnapshot current = getPresence(userId);
        PresenceSnapshot updated = PresenceSnapshot.withFlags(
            userId,
            current.wsConnected(),
            true,
            current.roomId(),
            LocalDateTime.now()
        );
        presenceRepository.save(updated, ttlSeconds);
    }

    public PresenceSnapshot getPresence(String userId) {
        PresenceSnapshot snapshot = presenceRepository.find(userId)
            .orElseGet(() -> PresenceSnapshot.initial(userId));
        log.info(
            "[PresenceService] getPresence userId={} ws={} sse={} roomId={}",
            userId,
            snapshot.wsConnected(),
            snapshot.sseConnected(),
            snapshot.roomId()
        );
        return snapshot;
    }

    public NotificationChannel decideMessageChannel(String userId, String roomId) {
        PresenceState state = toState(getPresence(userId));
        return state.decideMessageChannel(roomId);
    }

    public NotificationChannel decideRequestChannel(String userId, String roomId) {
        PresenceState state = toState(getPresence(userId));
        return state.decideRequestChannel(roomId);
    }

    private PresenceState toState(PresenceSnapshot snapshot) {
        if (snapshot.wsConnected() && snapshot.roomId() != null) {
            return new InRoomState(snapshot.roomId());
        }
        if (snapshot.sseConnected()) {
            return new AppActiveState();
        }
        return new AppInactiveState();
    }
}
