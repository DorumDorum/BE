package com.project.dorumdorum.domain.friend.domain.repository;

import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByFromUser(Long fromUser);
    List<FriendRequest> findByToUserAndStatus(Long toUser,  FriendRequestStatus status);
    List<FriendRequest> findByFromUserAndStatus(Long fromUser, FriendRequestStatus status);
    List<FriendRequest> findByFriendRequestNo(Long friendRequestNo);
    void deleteByFriendRequestNo(Long friendRequestNo);
}
