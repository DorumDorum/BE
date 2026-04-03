package com.project.dorumdorum.domain.chat.application.dto.response;

import java.time.LocalDateTime;

public record ChatReadReceiptResponse(
        String chatRoomNo,
        String readerUserNo,
        LocalDateTime readAt
) {}
