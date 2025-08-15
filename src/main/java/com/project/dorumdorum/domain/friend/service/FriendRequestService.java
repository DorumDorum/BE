package com.project.dorumdorum.domain.friend.service;

import com.project.dorumdorum.domain.friend.application.dto.response.FriendRequestListResponse;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;
import com.project.dorumdorum.domain.friend.domain.repository.FriendRequestRepository;
import com.project.dorumdorum.global.exception.RestApiException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    public void saveRequest(Long fromUser, Long toUser) {
        FriendRequest newFriendRequest = FriendRequest.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();
        friendRequestRepository.save(newFriendRequest);
    }

    public FriendRequest acceptRequest(Long toUser, Long requestNo) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        if(friendRequest.getToUser().equals(toUser) && friendRequest.getStatus().equals(FriendRequestStatus.PENDING)) {
            friendRequest.acceptRequest();
            return friendRequest;
        } else {
            throw new RestApiException(_NOT_FOUND);
        }
    }

    public void rejectRequest(Long toUser, Long requestNo) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        if(friendRequest.getToUser().equals(toUser) && friendRequest.getStatus().equals(FriendRequestStatus.PENDING)) {
            friendRequest.rejectRequest();
        } else {
            throw new RestApiException(_NOT_FOUND);
        }
    }

    public boolean existFriendRequestByFromUser(Long fromUser) {
        return (!friendRequestRepository.findByFromUserAndStatus(fromUser, FriendRequestStatus.PENDING).isEmpty());
    }

    public boolean existFriendRequestByRequestNo(Long requestNo) {
        return (!friendRequestRepository.findByFriendRequestNo(requestNo).isEmpty());
    }

    public boolean existFriendRequestByToUser(Long fromUser) {
        return (!friendRequestRepository.findByFromUserAndStatus(fromUser, FriendRequestStatus.PENDING).isEmpty());
    }

    public List<FriendRequestListResponse> getReceivedFriendRequestList(Long toUser) {
        return FriendRequestListResponse.create(friendRequestRepository.findByToUserAndStatus(toUser, FriendRequestStatus.PENDING));
    }


}
