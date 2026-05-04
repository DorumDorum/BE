package com.project.dorumdorum.global.security;

import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@DisplayName("ChatRoomAuthorizationInterceptor Unit Tests")
class ChatRoomAuthorizationInterceptorTest {

    private ChatRoomAuthorizationInterceptor interceptor;
    private MessageChannel channel;

    @BeforeEach
    void setUp() {
        interceptor = new ChatRoomAuthorizationInterceptor();
        channel = mock(MessageChannel.class);
    }

    // ── CONNECT ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("CONNECT — sessionAttributes에 userNo가 있으면 StompPrincipal 등록")
    void preSend_ConnectWithUserNo_RegistersPrincipal() {
        Message<byte[]> message = connectMessage(Map.of("userNo", "user-1"));

        Message<?> result = interceptor.preSend(message, channel);

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(result, StompHeaderAccessor.class);
        assertThat(accessor).isNotNull();
        assertThat(accessor.getUser()).isNotNull();
        assertThat(accessor.getUser().getName()).isEqualTo("user-1");
    }

    @Test
    @DisplayName("CONNECT — sessionAttributes가 없으면 원본 메시지 그대로 반환")
    void preSend_ConnectWithoutSessionAttributes_ReturnsOriginal() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        Message<?> result = interceptor.preSend(message, channel);

        assertThat(result).isNotNull();
        StompHeaderAccessor resultAccessor = MessageHeaderAccessor.getAccessor(result, StompHeaderAccessor.class);
        assertThat(resultAccessor.getUser()).isNull();
    }

    @Test
    @DisplayName("CONNECT — sessionAttributes에 userNo가 없으면 원본 메시지 그대로 반환")
    void preSend_ConnectWithoutUserNo_ReturnsOriginal() {
        Message<byte[]> message = connectMessage(new HashMap<>());

        Message<?> result = interceptor.preSend(message, channel);

        StompHeaderAccessor resultAccessor = MessageHeaderAccessor.getAccessor(result, StompHeaderAccessor.class);
        assertThat(resultAccessor.getUser()).isNull();
    }

    // ── SEND ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("SEND — 유효한 destination + sessionAttributes가 있으면 통과")
    void preSend_ValidSendWithSessionAttributes_PassesThrough() {
        Message<byte[]> message = sendMessage("/app/chat-room/cr-1/send", Map.of("userNo", "user-1"));

        Message<?> result = interceptor.preSend(message, channel);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("SEND — sessionAttributes가 없으면 NOT_CHAT_ROOM_MEMBER 예외")
    void preSend_SendWithoutSessionAttributes_ThrowsException() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SEND);
        accessor.setDestination("/app/chat-room/cr-1/send");
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        assertThatThrownBy(() -> interceptor.preSend(message, channel))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("SEND — sessionAttributes에 userNo가 없으면 NOT_CHAT_ROOM_MEMBER 예외")
    void preSend_SendWithoutUserNo_ThrowsException() {
        Message<byte[]> message = sendMessage("/app/chat-room/cr-1/send", new HashMap<>());

        assertThatThrownBy(() -> interceptor.preSend(message, channel))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("SEND — prefix가 맞지 않는 destination은 검증 없이 통과")
    void preSend_SendToUnrelatedDestination_PassesThrough() {
        Message<byte[]> message = sendMessage("/topic/chat-room/cr-1", new HashMap<>());

        Message<?> result = interceptor.preSend(message, channel);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("SEND — suffix가 /send로 끝나지 않으면 검증 없이 통과")
    void preSend_SendWithoutSendSuffix_PassesThrough() {
        Message<byte[]> message = sendMessage("/app/chat-room/cr-1/join", new HashMap<>());

        Message<?> result = interceptor.preSend(message, channel);

        assertThat(result).isNotNull();
    }

    // ── OTHER COMMANDS ────────────────────────────────────────────────────

    @Test
    @DisplayName("SUBSCRIBE 명령은 검증 없이 통과")
    void preSend_SubscribeCommand_PassesThrough() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        Message<?> result = interceptor.preSend(message, channel);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("DISCONNECT 명령은 검증 없이 통과")
    void preSend_DisconnectCommand_PassesThrough() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        Message<?> result = interceptor.preSend(message, channel);

        assertThat(result).isNotNull();
    }

    // ── helpers ──────────────────────────────────────────────────────────

    private Message<byte[]> connectMessage(Map<String, Object> sessionAttributes) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setSessionAttributes(sessionAttributes);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }

    private Message<byte[]> sendMessage(String destination, Map<String, Object> sessionAttributes) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SEND);
        accessor.setDestination(destination);
        accessor.setSessionAttributes(sessionAttributes);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }
}
