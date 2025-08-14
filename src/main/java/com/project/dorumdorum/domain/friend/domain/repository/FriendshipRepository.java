package com.project.dorumdorum.domain.friend.domain.repository;


import com.project.dorumdorum.domain.friend.domain.entity.Friendship;
import com.project.dorumdorum.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findByUserNoAndFriendUserNo(User userNo, User friendUserNo);
    List<Friendship> findByUserNo(User userNo);
}
