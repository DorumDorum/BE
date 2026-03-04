package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRequestRepository extends JpaRepository<RoomRequest, String>, RoomRequestQueryRepository {

    boolean existsByUserNoAndRoom(String userNo, Room room);
    Optional<RoomRequest> findByUserNoAndRoomAndDirection(String userNo, Room room, Direction direction);

}

