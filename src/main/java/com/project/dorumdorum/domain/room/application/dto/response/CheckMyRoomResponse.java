package com.project.dorumdorum.domain.room.application.dto.response;

public record CheckMyRoomResponse(
        Boolean isExist,
        String roomNo
) {
}
