package com.project.dorumdorum.domain.chat.application.event;

import lombok.Builder;

@Builder
public record MessageRequestCreatedEvent(
    String messageRoomId,
    String senderId,
    String receiverId,
    String senderNickname
) {
    public static MessageRequestCreatedEvent create(
        String messageRoomId,
        String senderId,
        String receiverId,
        String senderNickname
    ) {
        return MessageRequestCreatedEvent.builder()
            .messageRoomId(messageRoomId)
            .senderId(senderId)
            .receiverId(receiverId)
            .senderNickname(senderNickname)
            .build();
    }
}
