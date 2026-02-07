package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;

import java.util.List;

public interface RoomRequestRepositoryCustom {

    List<RoomRequestApplicationResponse> findApplicationsByRoom(Room room);
}
