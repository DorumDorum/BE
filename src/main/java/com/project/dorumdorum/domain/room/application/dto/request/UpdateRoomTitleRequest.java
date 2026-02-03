package com.project.dorumdorum.domain.room.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateRoomTitleRequest(
        @NotBlank String title
) {}
