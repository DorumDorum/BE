package com.project.dorumdorum.domain.roommate.domain.repository;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.roommate.domain.entity.Roommate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoommateRepository extends JpaRepository<Roommate, String>, RoommateQueryRepository {

    @Query("select (count(rm) > 0) " +
           "from Roommate rm " +
           "where rm.userNo = :userNo and rm.room.roomNo = :roomNo")
    Boolean existsByUserNoAndRoomNo(@Param("userNo") String userNo, @Param("roomNo") String roomNo);

    @Query("select rm from Roommate rm " +
           "where rm.userNo = :userNo and rm.room.roomNo = :roomNo")
    Optional<Roommate> findByUserNoAndRoomNo(@Param("userNo") String userNo, @Param("roomNo") String roomNo);

    List<Roommate> findAllByUserNo(String userNo);
    Optional<Roommate> findByUserNoAndRoom(String userNo, Room room);
    List<Roommate> findByRoom(Room room);
    Optional<Roommate> findByUserNo(String userNo);
    boolean existsByUserNo(String userNo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Roommate rm SET rm.deletedAt = CURRENT_TIMESTAMP WHERE rm.userNo = :userNo AND rm.deletedAt IS NULL")
    void deleteAllByUserNo(@Param("userNo") String userNo);

}
