package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoomRuleRepository extends MongoRepository<RoomRule, String> {

    Optional<RoomRule> findByRoomNo(String roomNo);

}
