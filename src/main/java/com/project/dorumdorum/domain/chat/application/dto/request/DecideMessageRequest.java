package com.project.dorumdorum.domain.chat.application.dto.request;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;

public record DecideMessageRequest(
    MessageRequestDecision messageRequestDecision
) {
}
