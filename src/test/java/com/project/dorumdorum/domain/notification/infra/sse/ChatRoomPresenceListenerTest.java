package com.project.dorumdorum.domain.notification.infra.sse;

import com.project.dorumdorum.domain.notification.domain.repository.UserPresenceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.HashMap;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatRoomPresenceListener 단위 테스트")
class ChatRoomPresenceListenerTest {

    @Mock
    private UserPresenceRepository userPresenceRepository;

    @Mock
    private SseEmitterRegistry sseEmitterRegistry;

    @InjectMocks
    private ChatRoomPresenceListener listener;

    @Test
    @DisplayName("채팅방 topic 구독 시 IN_CHATROOM 상태로 전환한다")
    void handleSubscribe_WhenChatRoomTopic_SetsInChatroom() {
        SessionSubscribeEvent event = new SessionSubscribeEvent(this, createSubscribeMessage("session-1", "user-1", "/topic/chat-room/cr-1"));

        listener.handleSubscribe(event);

        verify(userPresenceRepository).setInChatroom("user-1", "cr-1");
    }

    @Test
    @DisplayName("채팅방 topic이 아닌 구독은 무시한다")
    void handleSubscribe_WhenOtherTopic_Ignores() {
        SessionSubscribeEvent event = new SessionSubscribeEvent(this, createSubscribeMessage("session-1", "user-1", "/topic/notice"));

        listener.handleSubscribe(event);

        verify(userPresenceRepository, never()).setInChatroom(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("채팅방 구독 세션이 disconnect 되고 SSE 연결이 남아 있으면 ONLINE으로 복귀한다")
    void handleDisconnect_WhenSseConnected_SetsOnline() {
        listener.handleSubscribe(new SessionSubscribeEvent(this, createSubscribeMessage("session-1", "user-1", "/topic/chat-room/cr-1")));
        when(sseEmitterRegistry.hasConnection("user-1")).thenReturn(true);

        listener.handleDisconnect(new SessionDisconnectEvent(this, createDisconnectMessage("session-1"), "session-1", CloseStatus.NORMAL));

        verify(userPresenceRepository).setOnline("user-1");
        verify(userPresenceRepository, never()).setOffline("user-1");
    }

    @Test
    @DisplayName("채팅방 구독 세션이 disconnect 되고 SSE 연결도 없으면 OFFLINE으로 전환한다")
    void handleDisconnect_WhenNoSseConnection_SetsOffline() {
        listener.handleSubscribe(new SessionSubscribeEvent(this, createSubscribeMessage("session-1", "user-1", "/topic/chat-room/cr-1")));
        when(sseEmitterRegistry.hasConnection("user-1")).thenReturn(false);

        listener.handleDisconnect(new SessionDisconnectEvent(this, createDisconnectMessage("session-1"), "session-1", CloseStatus.NORMAL));

        verify(userPresenceRepository).setOffline("user-1");
        verify(userPresenceRepository, never()).setOnline("user-1");
    }

    private Message<byte[]> createSubscribeMessage(String sessionId, String userNo, String destination) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setSessionId(sessionId);
        accessor.setDestination(destination);
        accessor.setSessionAttributes(new HashMap<>());
        accessor.getSessionAttributes().put("userNo", userNo);
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }

    private Message<byte[]> createDisconnectMessage(String sessionId) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
        accessor.setSessionId(sessionId);
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }
}
