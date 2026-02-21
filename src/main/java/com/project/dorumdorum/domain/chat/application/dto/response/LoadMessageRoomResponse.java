package com.project.dorumdorum.domain.chat.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadMessageRoomResponse(
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String messageRoomNo,

    MessageRoomType roomType,

    MessageRoomStatus roomStatus,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    String messageRequestNo,

    String lastMessage,

    LocalDateTime lastMessageAt,
    boolean hasUnread,
    boolean isRequester
) {
}
