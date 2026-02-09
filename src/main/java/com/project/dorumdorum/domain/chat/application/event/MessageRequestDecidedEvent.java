package com.project.dorumdorum.domain.chat.application.event;

import com.project.dorumdorum.domain.chat.application.dto.request.MessageRequestDecision;
import lombok.Builder;

@Builder
public record MessageRequestDecidedEvent(
    String roomId,
    String senderId,
    String receiverId,
    MessageRequestDecision decision
) {
}
