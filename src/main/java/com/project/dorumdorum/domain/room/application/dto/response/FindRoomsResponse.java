package com.project.dorumdorum.domain.room.application.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindRoomsResponse(
        String roomNo,
        RoomType roomType,
        Integer capacity,
        Integer currentMateCount,
        LocalDateTime createdAt,
        String title,
        String hostNickname,
        RoomStatus roomStatus,
        Boolean isHost,
        String residencePeriod // 거주기간 (예: "학기(16주)", "반기(24주)", "계절학기")
) {}