package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.global.pagination.DecodedCursor;

import java.util.List;
import java.util.Optional;

public interface RoomRepositoryCustom {

    List<FindRoomsResponse> searchByCursor(RoomRelation relation, List<RoomType> types, List<Integer> capacities, List<ResidencePeriod> residencePeriods, RoomSort sort, DecodedCursor decodedCursor, int limitPlusOne);

    Optional<FindRoomsResponse> findMyRoom(String userNo);

    /**
     * 사용자가 관심(좋아요) 표시한 방 목록을 조회한다.
     */
    List<FindRoomsResponse> findLikedRooms(String userNo);

    /**
     * 사용자가 지원한 방(RoomRequest.USER_TO_ROOM) 목록을 조회한다.
     */
    List<FindRoomsResponse> findAppliedRooms(String userNo);
}
