package com.project.dorumdorum.domain.checklist.domain.repository;

import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRuleRepository extends JpaRepository<RoomRule, String> {

    Optional<RoomRule> findByRoomNo(String roomNo);

}
