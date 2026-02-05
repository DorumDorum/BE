package com.project.dorumdorum.domain.room.application.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoomRequestApplicationResponse(
        String requestNo,
        String userNo,
        String name,
        String nickname,
        String major,
        String studentNo,
        String grade,
        Integer age,
        LocalDateTime createdAt,
        String introduction,
        String additionalMessage
) {}
