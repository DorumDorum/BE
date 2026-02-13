package com.project.dorumdorum.domain.chat.application.event;

import lombok.Builder;

@Builder
public record MessageRequestCreatedEvent(
    String roomId,
    String senderId,
    String receiverId,
    String senderNickname
) {
    public static MessageRequestCreatedEvent create(
        String roomId,
        String senderId,
        String receiverId,
        String senderNickname
    ) {
        return MessageRequestCreatedEvent.builder()
            .roomId(roomId)
            .senderId(senderId)
            .receiverId(receiverId)
            .senderNickname(senderNickname)
            .build();
    }
}
