package com.project.dorumdorum.domain.checklist.domain.repository;

import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRuleRepository extends JpaRepository<RoomRule, String> {

    @Query("select rr from RoomRule rr where rr.room.roomNo = :roomNo")
    Optional<RoomRule> findByRoomNo(@Param("roomNo") String roomNo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE RoomRule rr SET rr.deletedAt = CURRENT_TIMESTAMP WHERE rr.room.roomNo = :roomNo AND rr.deletedAt IS NULL")
    void deleteByRoomNo(@Param("roomNo") String roomNo);

    @Query("SELECT rr FROM RoomRule rr JOIN FETCH rr.room r WHERE r.roomStatus = 'CONFIRM_PENDING' AND r.deletedAt IS NULL AND rr.deletedAt IS NULL")
    List<RoomRule> findAllActiveWithRoom();
}
