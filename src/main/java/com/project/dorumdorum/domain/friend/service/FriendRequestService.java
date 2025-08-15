package com.project.dorumdorum.domain.friend.service;

import com.project.dorumdorum.domain.friend.application.dto.response.FriendRequestListResponse;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;
import com.project.dorumdorum.domain.friend.domain.repository.FriendRequestRepository;
import com.project.dorumdorum.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;

    public void saveRequest(User fromUser, User toUser) {
        FriendRequest newFriendRequest = FriendRequest.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();
        friendRequestRepository.save(newFriendRequest);
    }

    public boolean existFriendRequest(User fromUser) {
        return (!friendRequestRepository.findByFromUserAndStatus(fromUser, FriendRequestStatus.PENDING).isEmpty());
    }

    public boolean areAlreadyFriends(User fromUser, User toUser) {
        return (!friendRequestRepository.findByFromUserAndToUser(fromUser, toUser).isEmpty());
    }

    public List<FriendRequestListResponse> getReceivedFriendRequestList(User toUser) {
        return FriendRequestListResponse.create(friendRequestRepository.findByToUser(toUser));
    }
}
