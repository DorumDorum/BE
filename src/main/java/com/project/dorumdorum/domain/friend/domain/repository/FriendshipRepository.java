package com.project.dorumdorum.domain.friend.domain.repository;

import com.project.dorumdorum.domain.friend.domain.entity.Friendship;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findByUserNoAndFriendUserNo(Long userNo, Long friendUserNo);
    List<Friendship> findByUserNo(Long userNo);
}
