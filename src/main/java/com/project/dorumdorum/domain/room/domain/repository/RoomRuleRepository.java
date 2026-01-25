package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRuleRepository extends JpaRepository<RoomRule, Long> {

    @Query("SELECT rr FROM RoomRule rr " +
            "LEFT JOIN FETCH rr.items i " +
            "LEFT JOIN FETCH i.options o " +
            "WHERE rr.room.roomNo = :roomNo")
    Optional<RoomRule> findByRoomNoWithAll(@Param("roomNo") Long roomNo);

    Optional<RoomRule> findByRoom_RoomNo(Long roomNo);

    boolean existsByRoom_RoomNo(Long roomNo);
}
