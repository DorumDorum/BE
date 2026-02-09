package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.domain.entity.Message;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MessageSentEvent(
        String messageId,
        String roomId,
        String senderId,
        String senderName,  // 추가
        String content,
        MessageType messageType,
        LocalDateTime sentAt
) {
    public static MessageSentEvent create(Message message, String senderName) {
        return MessageSentEvent.builder()
            .messageId(message.getMessageNo())
            .roomId(message.getMessageRoomNo())
            .senderId(message.getSenderNo())
            .senderName(senderName)  // 추가
            .content(message.getContent())
            .messageType(message.getMessageType())
            .sentAt(message.getSentAt())
            .build();
    }
}
