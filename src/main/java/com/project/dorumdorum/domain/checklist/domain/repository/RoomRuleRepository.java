package com.project.dorumdorum.domain.checklist.domain.repository;

import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRuleRepository extends JpaRepository<RoomRule, String> {

    @Query("select rr from RoomRule rr where rr.room.roomNo = :roomNo")
    Optional<RoomRule> findByRoomNo(@Param("roomNo") String roomNo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM RoomRule rr WHERE rr.room.roomNo = :roomNo")
    void deleteByRoomNo(@Param("roomNo") String roomNo);
}
