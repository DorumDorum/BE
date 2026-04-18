package com.project.dorumdorum.global.security;

import com.project.dorumdorum.global.exception.RestApiException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.project.dorumdorum.global.exception.code.status.ChatErrorStatus.NOT_CHAT_ROOM_MEMBER;

@Component
public class ChatRoomAuthorizationInterceptor implements ChannelInterceptor {

    private static final String SEND_PREFIX = "/app/chat-room/";
    private static final String SEND_SUFFIX = "/send";

    /**
     * CONNECT: handshake 때 저장한 userNo를 Spring principal로 등록.
     *   → convertAndSendToUser(userNo, "/queue/...", payload) 라우팅에 사용됨.
     * SEND: /app/chat-room/{chatRoomNo}/send 경로의 userNo 세션 속성 유효성 검증.
     *   채팅방 멤버십 검증은 SendGroupChatMessageUseCase에서 수행.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        if (accessor.getCommand() == StompCommand.CONNECT) {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes != null) {
                String userNo = (String) sessionAttributes.get("userNo");
                if (userNo != null) {
                    StompHeaderAccessor mutableAccessor = StompHeaderAccessor.wrap(message);
                    mutableAccessor.setUser(new StompPrincipal(userNo));
                    return MessageBuilder.createMessage(message.getPayload(), mutableAccessor.getMessageHeaders());
                }
            }
            return message;
        }

        if (accessor.getCommand() != StompCommand.SEND) return message;

        String destination = accessor.getDestination();
        if (destination == null
                || !destination.startsWith(SEND_PREFIX)
                || !destination.endsWith(SEND_SUFFIX)) return message;

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes == null) throw new RestApiException(NOT_CHAT_ROOM_MEMBER);

        String userNo = (String) sessionAttributes.get("userNo");
        if (userNo == null) throw new RestApiException(NOT_CHAT_ROOM_MEMBER);

        return message;
    }
}
