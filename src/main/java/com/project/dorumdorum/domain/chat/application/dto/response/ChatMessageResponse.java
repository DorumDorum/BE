package com.project.dorumdorum.domain.chat.application.dto.response;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        String messageNo,
        String chatRoomNo,
        String senderNo,
        String content,
        String messageType,
        LocalDateTime sentAt
) {}
