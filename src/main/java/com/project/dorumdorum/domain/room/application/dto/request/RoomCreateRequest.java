package com.project.dorumdorum.domain.room.application.dto.request;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateRoomRuleRequest;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomCreateRequest(
        @NotNull RoomType roomType,
        @NotNull Integer capacity,
        @NotNull ResidencePeriod residencePeriod,
        @NotBlank String title,
        @NotNull CreateRoomRuleRequest rule
) {}
