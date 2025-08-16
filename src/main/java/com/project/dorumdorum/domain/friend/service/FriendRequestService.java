package com.project.dorumdorum.domain.friend.service;

import com.project.dorumdorum.domain.friend.application.dto.response.FriendRequestListResponse;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;
import com.project.dorumdorum.domain.friend.domain.repository.FriendRequestRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    public FriendRequest findById(Long friendRequestNo) {
        return friendRequestRepository.findById(friendRequestNo)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    public void saveRequest(Long fromUser, Long toUser) {
        FriendRequest newFriendRequest = FriendRequest.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();
        friendRequestRepository.save(newFriendRequest);
    }

    public void acceptRequest(FriendRequest friendRequest) {
        friendRequest.acceptRequest();
    }

    public void rejectRequest(FriendRequest friendRequest) {
        friendRequest.rejectRequest();
    }

    public void cancelRequest(FriendRequest friendRequest) {
        friendRequestRepository.deleteByFriendRequestNo(friendRequest.getFriendRequestNo());
    }

    public List<FriendRequestListResponse> getReceivedFriendRequestList(Long toUser) {
        return FriendRequestListResponse.create(friendRequestRepository.findByToUserAndStatus(toUser, FriendRequestStatus.PENDING));
    }

    public List<FriendRequestListResponse> getSentFriendRequestList(Long fromUser) {
        return FriendRequestListResponse.create(friendRequestRepository.findByFromUserAndStatus(fromUser,  FriendRequestStatus.PENDING));
    }

    public boolean existFriendRequestByFromUser(Long fromUser) {
        return (!friendRequestRepository.findByFromUserAndStatus(fromUser, FriendRequestStatus.PENDING).isEmpty());
    }

    public boolean existFriendRequestByRequestNo(Long friendRequestNo) {
        return (!friendRequestRepository.findByFriendRequestNo(friendRequestNo).isEmpty());
    }

    public boolean existFriendRequestByToUser(Long fromUser) {
        return (!friendRequestRepository.findByFromUserAndStatus(fromUser, FriendRequestStatus.PENDING).isEmpty());
    }
}
