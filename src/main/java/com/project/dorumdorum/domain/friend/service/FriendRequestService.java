package com.project.dorumdorum.domain.friend.service;

import com.project.dorumdorum.domain.friend.application.dto.response.FriendRequestListResponse;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;
import com.project.dorumdorum.domain.friend.domain.repository.FriendRequestRepository;
import com.project.dorumdorum.domain.user.domain.entity.User;

import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

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

    public void acceptRequest(User toUser, Long requestNo) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        if(friendRequest.getToUser().equals(toUser)) {
            friendRequest.acceptRequest(requestNo);
        }
    }

    public void rejectRequest(User toUser, Long requestNo) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        if(friendRequest.getToUser().equals(toUser)) {
            friendRequest.rejectRequest(requestNo);
        }
    }

    public boolean existFriendRequestByFromUser(User fromUser) {
        return (!friendRequestRepository.findByFromUserAndStatus(fromUser, FriendRequestStatus.PENDING).isEmpty());
    }

    public boolean existFriendRequestByRequestNo(Long requestNo) {
        return (!friendRequestRepository.findByFriendRequestNo(requestNo).isEmpty());
    }

    public boolean existFriendRequestByToUser(User fromUser) {
        return (!friendRequestRepository.findByFromUserAndStatus(fromUser, FriendRequestStatus.PENDING).isEmpty());
    }

    public boolean areAlreadyFriends(User fromUser, User toUser) {
        return (!friendRequestRepository.findByFromUserAndToUser(fromUser, toUser).isEmpty());
    }

    public List<FriendRequestListResponse> getReceivedFriendRequestList(User toUser) {
        return FriendRequestListResponse.create(friendRequestRepository.findByToUserAndStatus(toUser, FriendRequestStatus.PENDING));
    }


}
