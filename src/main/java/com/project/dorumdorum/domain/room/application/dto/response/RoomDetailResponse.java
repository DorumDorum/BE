package com.project.dorumdorum.domain.room.application.dto.response;

import com.project.dorumdorum.domain.room.domain.entity.RoomStatus;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;

public record RoomDetailResponse(
        String roomNo,
        RoomType roomType,
        Integer capacity,
        Integer currentMateCount,
        Integer remaining,
        String title,
        String notes,
        String hostUserNo,
        String hostName,
        String hostNickname,
        String hostMajor,
        String hostStudentYear,
        String residencePeriod,
        RoomStatus roomStatus,
        boolean liked,
        AppliedStatus appliedStatus,
        boolean isMyRoom
) {
    public enum AppliedStatus {
        NONE,
        WAITING,
        APPROVED
    }
}
