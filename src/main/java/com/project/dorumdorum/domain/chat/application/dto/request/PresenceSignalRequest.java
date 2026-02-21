package com.project.dorumdorum.domain.chat.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PresenceSignalRequest(
    @NotNull String messageRoomNo,
    String lastReadMessageId,
    LocalDateTime lastReadSentAt
) {
}
