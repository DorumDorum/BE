package com.project.dorumdorum.domain.chat.application.dto.response;

import java.time.LocalDateTime;

public record ChatMessageSummary(
        String messageNo,
        String senderNo,
        String senderNickname,
        String content,
        String messageType,
        LocalDateTime sentAt,
        int unreadCount
) {}
