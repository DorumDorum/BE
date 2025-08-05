package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.Roommate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoommateRepository extends JpaRepository<Roommate, Long> {

    Boolean existsByUserNoAndRoom(Long userNo, Room room);
    List<Roommate> findByUserNo(Long userNo);
    Optional<Roommate> findByUserNoAndRoom(Long userNo, Room room);

}
