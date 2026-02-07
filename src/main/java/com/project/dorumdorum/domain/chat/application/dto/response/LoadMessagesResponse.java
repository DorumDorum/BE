package com.project.dorumdorum.domain.chat.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.dorumdorum.domain.chat.domain.entity.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoadMessagesResponse(
    List<MessageDto> messages,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Long nextCursor,
    boolean hasMore
) {
    @Builder
    public record MessageDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Long messageNo,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Long senderNo,
        String senderName,
        String content,
        MessageType messageType,
        LocalDateTime sentAt,
        Integer readCount
    ) {
    }
}
