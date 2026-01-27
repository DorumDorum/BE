package com.project.dorumdorum.domain.room.application.dto.response;

import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindRoomsResponse(
        Long roomNo,
        RoomType roomType,
        Integer capacity,
        Integer currentMateCount,
        LocalDateTime createdAt,
        String title,
        String hostNickname,
        RoomStatus roomStatus,
        Boolean isHost
) {}