package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.Roommate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoommateRepository extends JpaRepository<Roommate, Long> {
    Boolean existsByUserNoAndRoom(Long userNo, Room room);

    List<Roommate> findByUserNo(Long userNo);
}
