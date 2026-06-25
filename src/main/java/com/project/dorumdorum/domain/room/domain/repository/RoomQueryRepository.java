package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.user.domain.entity.Gender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomQueryRepository {

    List<FindRoomsResponse> findByCursor(
            Gender gender,
            ChecklistFilterRequest request,
            LocalDateTime cursorCreatedAt,
            String cursorId,
            Integer cursorRemaining,
            int limitPlusOne
    );

    long countByFilter(Gender gender, ChecklistFilterRequest request);

    Optional<FindRoomsResponse> findMyRoom(String userNo);

    List<FindRoomsResponse> findLikedRooms(String userNo);

    List<FindRoomsResponse> findAppliedRooms(String userNo);
}
