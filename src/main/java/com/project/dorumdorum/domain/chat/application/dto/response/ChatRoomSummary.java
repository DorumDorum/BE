package com.project.dorumdorum.domain.chat.application.dto.response;

import java.time.LocalDateTime;

public record ChatRoomSummary(
        String chatRoomNo,
        String roomNo,
        String lastMessageContent,
        LocalDateTime lastMessageAt,
        long unreadCount
) {}
