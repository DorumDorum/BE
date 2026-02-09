package com.project.dorumdorum.domain.chat.application.event;

import lombok.Builder;

@Builder
public record MessageRequestCreatedEvent(
    String roomId,
    String senderId,
    String receiverId,
    String senderNickname
) {
}
