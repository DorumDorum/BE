package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomLikeRepository extends JpaRepository<RoomLike, String> {

    boolean existsByUserNoAndRoom(String userNo, Room room);
    void deleteByUserNoAndRoom(String userNo, Room room);

    // N+1 DELETE 문제 — @Modifying JPQL 벌크 DELETE로 교체
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM RoomLike l WHERE l.room = :room")
    void deleteAllByRoom(@Param("room") Room room);
}

