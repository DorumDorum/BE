package com.project.dorumdorum.domain.room.application.dto.response;

import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoadRoomsResponse (
        Long roomNo,
        RoomType roomType,
        Integer capacity,
        Integer currentMateCount,
        LocalDateTime createdAt,
        String title,
        String hostNickname,
        List<Tag> additionalTag
) {}