package com.project.dorumdorum.domain.friend.domain.repository;

import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;
import com.project.dorumdorum.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByFromUser(User fromUser);
    List<FriendRequest> findByToUser(User toUser);
    List<FriendRequest> findByFromUserAndToUser(User fromUser, User toUser);
    List<FriendRequest> findByFromUserAndStatus(User fromUser, FriendRequestStatus status);
}
