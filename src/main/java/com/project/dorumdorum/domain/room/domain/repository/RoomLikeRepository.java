package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomLikeRepository extends JpaRepository<RoomLike, String> {

    boolean existsByUserNoAndRoom(String userNo, Room room);

    void deleteByUserNoAndRoom(String userNo, Room room);
}

