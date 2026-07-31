package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRequestRepository extends JpaRepository<RoomRequest, String>, RoomRequestQueryRepository {

    boolean existsByUserNoAndRoom(String userNo, Room room);
    Optional<RoomRequest> findByUserNoAndRoomAndDirection(String userNo, Room room, Direction direction);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE RoomRequest r SET r.deletedAt = CURRENT_TIMESTAMP WHERE r.room = :room AND r.deletedAt IS NULL")
    void deleteAllByRoom(@Param("room") Room room);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE RoomRequest r SET r.deletedAt = CURRENT_TIMESTAMP WHERE r.userNo = :userNo AND r.deletedAt IS NULL")
    void deleteAllByUserNo(@Param("userNo") String userNo);
}
