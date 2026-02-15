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
}
