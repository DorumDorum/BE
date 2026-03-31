package com.project.dorumdorum.domain.notification.infra.sse;

import com.project.dorumdorum.domain.notification.domain.repository.UserPresenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatRoomPresenceListener {

    private static final String CHAT_ROOM_TOPIC_PREFIX = "/topic/chat-room/";

    private final UserPresenceRepository userPresenceRepository;
    private final SseEmitterRegistry sseEmitterRegistry;

    private final Map<String, ChatRoomSession> chatRoomSessions = new ConcurrentHashMap<>();

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();
        String userNo = resolveUserNo(accessor);

        if (sessionId == null || userNo == null || destination == null || !destination.startsWith(CHAT_ROOM_TOPIC_PREFIX)) {
            return;
        }

        String chatRoomNo = destination.substring(CHAT_ROOM_TOPIC_PREFIX.length());
        if (chatRoomNo.isBlank()) {
            return;
        }

        chatRoomSessions.put(sessionId, new ChatRoomSession(userNo, chatRoomNo));
        userPresenceRepository.setInChatroom(userNo, chatRoomNo);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        if (sessionId == null) {
            return;
        }

        ChatRoomSession session = chatRoomSessions.remove(sessionId);
        if (session == null) {
            return;
        }

        if (sseEmitterRegistry.hasConnection(session.userNo())) {
            userPresenceRepository.setOnline(session.userNo());
            return;
        }

        userPresenceRepository.setOffline(session.userNo());
    }

    private String resolveUserNo(StompHeaderAccessor accessor) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes == null) {
            return null;
        }
        Object userNo = sessionAttributes.get("userNo");
        return userNo instanceof String value ? value : null;
    }

    private record ChatRoomSession(String userNo, String chatRoomNo) {
    }
}
