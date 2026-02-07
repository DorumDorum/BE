package com.project.dorumdorum.domain.roommate.application.dto.response;

import com.project.dorumdorum.domain.roommate.domain.entity.ConfirmStatus;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.user.domain.entity.Gender;

public record MyRoommateResponse(
        String roommateNo,
        String userNo,
        ConfirmStatus confirmStatus,
        RoomRole roomRole,
        String name,
        String nickname,
        String studentNo,
        String major,
        String grade,
        Integer age,
        Gender gender,
        Boolean isMe
) {
}
