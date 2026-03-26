package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.request.SendChatMessageRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

/**
 * STOMP WebSocket 채팅 메시지 엔드포인트 명세
 * 클라이언트 → 서버: /app/chat-room/{chatRoomNo}/send
 * 서버 → 클라이언트: /topic/chat-room/{chatRoomNo}
 */
public interface ChatMessageApiSpec {

    @MessageMapping("/chat-room/{chatRoomNo}/send")
    void send(@DestinationVariable String chatRoomNo,
              @Payload SendChatMessageRequest request,
              SimpMessageHeaderAccessor headerAccessor);
}
