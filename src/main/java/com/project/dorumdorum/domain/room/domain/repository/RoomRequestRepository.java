package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRequestRepository extends JpaRepository<RoomRequest, Long> {

    boolean existsByUserNoAndRoom(Long userNo, Room room);

}
