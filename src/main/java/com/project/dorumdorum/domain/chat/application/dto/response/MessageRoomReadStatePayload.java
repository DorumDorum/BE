package com.project.dorumdorum.domain.chat.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record MessageRoomReadStatePayload(
    String messageRoomNo,
    int inMessageRoomCount,
    List<ParticipantReadState> participants
) {
    public record ParticipantReadState(
        String userId,
        String lastReadMessageId,
        LocalDateTime lastReadSentAt
    ) {
    }
}
