package com.project.dorumdorum.domain.chat.application.event;

import lombok.Builder;

@Builder
public record MessageRequestCreatedEvent(
    Long roomId,
    Long senderId,
    Long receiverId,
    String senderNickname
) {
}
