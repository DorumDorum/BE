package com.project.dorumdorum.domain.roommate.application.dto.response;

import com.project.dorumdorum.domain.room.domain.entity.RoomType;

import java.time.LocalDateTime;

public record RoommateHistoryResponse(
        String historyNo,
        String roomNo,
        String roomTitle,
        RoomType roomType,
        Integer capacity,
        String roommateUserNo,
        String name,
        String nickname,
        String studentNo,
        String major,
        String studentYear,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Relation relation
) {
    public enum Relation {
        CURRENT,
        PAST
    }
}
