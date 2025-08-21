package com.project.dorumdorum.domain.room.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InviteRoomRequest(
        @NotBlank String introduction
) {}
