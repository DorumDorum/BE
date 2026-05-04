package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String>, RoomQueryRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select room from Room room where room.roomNo = :roomNo and room.deletedAt is null")
    Optional<Room> findByRoomNoForUpdate(@Param("roomNo") String roomNo);

}
