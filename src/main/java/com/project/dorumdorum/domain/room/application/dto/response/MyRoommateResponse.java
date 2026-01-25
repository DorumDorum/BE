package com.project.dorumdorum.domain.room.application.dto.response;

import com.project.dorumdorum.domain.room.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomRole;
import com.project.dorumdorum.domain.user.domain.entity.Gender;

public record MyRoommateResponse(
        Long roommateNo,
        ConfirmStatus confirmStatus,
        RoomRole roomRole,
        String name,
        String nickname,
        String studentNo,
        Gender gender
) {
}
