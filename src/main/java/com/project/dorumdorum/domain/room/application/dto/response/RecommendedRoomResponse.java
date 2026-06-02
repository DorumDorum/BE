package com.project.dorumdorum.domain.room.application.dto.response;

import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import lombok.Builder;

import java.util.List;

@Builder
public record RecommendedRoomResponse(
        String roomNo,
        String title,
        RoomType roomType,
        Integer capacity,
        Integer currentMateCount,
        Integer remaining,
        RoomStatus roomStatus,
        String residencePeriod,
        String hostNickname,
        String matchLabel,
        int matchedCount,
        int totalCount,
        List<String> highlights
) {}
