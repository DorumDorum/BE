package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.Roommate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoommateRepository extends JpaRepository<Roommate, Long>, RoommateRepositoryCustom {

    @Query("select (count(rm) > 0) " +
           "from Roommate rm " +
           "where rm.userNo = :userNo and rm.room.roomNo = :roomNo")
    Boolean existsByUserNoAndRoomNo(@Param("userNo") Long userNo, @Param("roomNo") Long roomNo);
    List<Roommate> findAllByUserNo(Long userNo);
    Optional<Roommate> findByUserNoAndRoom(Long userNo, Room room);
    List<Roommate> findByRoom(Room room);
    Optional<Roommate> findByUserNo(Long userNo);
    boolean existsByUserNo(Long userNo);

}
