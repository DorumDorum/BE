package com.project.dorumdorum.domain.room.application.dto.request;

import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoomCreateRequest(
        @NotNull RoomType roomType,
        @NotNull Integer capacity,
        @NotBlank String title,
        @NotNull List<Tag> tags
) {}
