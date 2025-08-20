package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.LoadRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;
import com.project.dorumdorum.global.pagination.DecodedCursor;

import java.util.List;

public interface RoomRepositoryCustom {

    List<LoadRoomsResponse> findByCursor(Long userNo, RoomRelation relation, List<Tag> tags, RoomType type, Integer capacity, RoomSort sort, DecodedCursor decodedCursor, int limitPlusOne);

}
