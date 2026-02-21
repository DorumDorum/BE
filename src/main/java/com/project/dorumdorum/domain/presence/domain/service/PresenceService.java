package com.project.dorumdorum.domain.presence.domain.service;

import com.project.dorumdorum.domain.chat.application.event.MessageRoomReadStateChangedEvent;
import com.project.dorumdorum.domain.chat.domain.service.MessageService;
import com.project.dorumdorum.domain.chat.domain.service.ParticipantService;
import com.project.dorumdorum.domain.notification.domain.NotificationChannel;
import com.project.dorumdorum.domain.presence.domain.repository.PresenceRepository;
import com.project.dorumdorum.domain.presence.domain.entity.PresenceSnapshot;
import com.project.dorumdorum.domain.presence.domain.state.AppActiveState;
import com.project.dorumdorum.domain.presence.domain.state.AppInactiveState;
import com.project.dorumdorum.domain.presence.domain.state.InRoomState;
import com.project.dorumdorum.domain.presence.domain.state.PresenceState;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService {

    private final PresenceRepository presenceRepository;
    private final MessageService messageService;
    private final ParticipantService participantService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${presence.ttl-seconds:300}")
    private long ttlSeconds;

    public void onMessageRoomEnter(String userId, String messageRoomNo, String lastReadMessageId, LocalDateTime lastReadSentAt) {
        log.info("[Presence] ENTER userId={} messageRoomNo={}", userId, messageRoomNo);
        participantService.updateLastRead(userId, messageRoomNo, lastReadMessageId, lastReadSentAt);

        PresenceSnapshot current = getPresence(userId);
        PresenceSnapshot updated = PresenceSnapshot.withRoom(
            userId,
            messageRoomNo,
            true,
            current.sseConnected()
        );
        presenceRepository.save(updated, ttlSeconds);
        publishMessageRoomReadStateChanged(messageRoomNo, "ENTER", userId);
    }

    public void onMessageRoomLeave(String userId, String messageRoomNo, String lastReadMessageId, LocalDateTime lastReadSentAt) {
        log.info("[Presence] LEAVE userId={} messageRoomNo={}", userId, messageRoomNo);
        participantService.updateLastRead(userId, messageRoomNo, lastReadMessageId, lastReadSentAt);

        PresenceSnapshot current = getPresence(userId);
        PresenceSnapshot updated = PresenceSnapshot.withFlags(
            userId,
            current.wsConnected(),
            current.sseConnected(),
            null,
            LocalDateTime.now()
        );
        presenceRepository.save(updated, ttlSeconds);
        publishMessageRoomReadStateChanged(messageRoomNo, "LEAVE", userId);
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
        String currentMessageRoomNo = current.roomId();
        flushLastReadOnDisconnect(userId, currentMessageRoomNo);
        PresenceSnapshot updated = PresenceSnapshot.withFlags(
            userId,
            false,
            current.sseConnected(),
            null,
            LocalDateTime.now()
        );
        presenceRepository.save(updated, ttlSeconds);
        publishMessageRoomReadStateChanged(currentMessageRoomNo, "WS_DISCONNECT", userId);
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
        String currentMessageRoomNo = current.roomId();
        flushLastReadOnDisconnect(userId, currentMessageRoomNo);
        PresenceSnapshot updated = PresenceSnapshot.withFlags(
            userId,
            current.wsConnected(),
            false,
            current.roomId(),
            LocalDateTime.now()
        );
        presenceRepository.save(updated, ttlSeconds);
        publishMessageRoomReadStateChanged(currentMessageRoomNo, "SSE_DISCONNECT", userId);
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

    public void onWsActivity(String userId) {
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

    public void clear(String userId) {
        log.info("[Presence] CLEAR userId={}", userId);
        presenceRepository.delete(userId);
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

    private void publishMessageRoomReadStateChanged(String messageRoomNo, String trigger, String actorUserId) {
        if (messageRoomNo == null || messageRoomNo.isBlank()) {
            return;
        }
        applicationEventPublisher.publishEvent(
            MessageRoomReadStateChangedEvent.of(messageRoomNo, trigger, actorUserId)
        );
    }

    private void flushLastReadOnDisconnect(String userId, String messageRoomNo) {
        if (messageRoomNo == null || messageRoomNo.isBlank()) {
            return;
        }
        try {
            messageService.findLatestMessage(messageRoomNo)
                .ifPresent(message -> participantService.updateLastRead(
                    userId,
                    messageRoomNo,
                    message.getMessageNo(),
                    message.getSentAt()
                ));
        } catch (RestApiException e) {
            log.warn("[Presence] disconnect read flush skipped userId={} messageRoomNo={}", userId, messageRoomNo);
        }
    }
}
