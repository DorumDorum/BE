package com.project.dorumdorum.domain.friend.domain.repository;

import com.project.dorumdorum.domain.friend.domain.entity.Friendship;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Friendship f " + "where (f.userNo = :a and f.friendUserNo = :b) " + "or" + " (f.userNo = :b and f.friendUserNo = :a)")
    void deleteByUserNoAndFriendUserNo(@Param("a") Long a, @Param("b") Long b);

    @Query("select f from Friendship f " + "where (f.userNo = :a) " + "or" + " (f.friendUserNo = :a)")
    List<Friendship> getFriendListByUserNo(@Param("a") Long a); 

    List<Friendship> findByUserNoAndFriendUserNo(Long userNo, Long friendUserNo);
}
