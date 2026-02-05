package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomLikeRepository extends JpaRepository<RoomLike, Long> {

    boolean existsByUserNoAndRoom(Long userNo, Room room);

    void deleteByUserNoAndRoom(Long userNo, Room room);
}

