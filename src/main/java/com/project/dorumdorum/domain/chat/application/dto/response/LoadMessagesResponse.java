package com.project.dorumdorum.domain.chat.application.dto.response;

import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoadMessagesResponse(
    List<MessageDto> messages,
    String nextCursor,
    boolean hasMore
) {
    @Builder
    public record MessageDto(
        String messageNo,
        String senderNo,
        String senderName,
        String content,
        MessageType messageType,
        LocalDateTime sentAt
    ) {
    }
}
