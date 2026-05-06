package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomLikeRepository extends JpaRepository<RoomLike, String> {

    boolean existsByUserNoAndRoom(String userNo, Room room);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE RoomLike l SET l.deletedAt = CURRENT_TIMESTAMP WHERE l.userNo = :userNo AND l.room = :room AND l.deletedAt IS NULL")
    void deleteByUserNoAndRoom(@Param("userNo") String userNo, @Param("room") Room room);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE RoomLike l SET l.deletedAt = CURRENT_TIMESTAMP WHERE l.room = :room AND l.deletedAt IS NULL")
    void deleteAllByRoom(@Param("room") Room room);
}

