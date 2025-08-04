package com.project.dorumdorum.domain.room.application.dto.request;

public record JoinRoomRequest (
        String introduction,
        String additionalMessage
) {}
