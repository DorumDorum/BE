package com.project.dorumdorum.domain.chat.application.dto.response;

import lombok.Builder;

@Builder
public record LoadMessageRoomParticipantResponse(
    String profileImageUrl,
    String userId,
    String name,
    String studentNo,
    String major,
    Integer age
) {
}
