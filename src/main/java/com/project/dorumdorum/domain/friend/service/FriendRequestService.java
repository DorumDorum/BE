package com.project.dorumdorum.domain.friend.service;

import com.project.dorumdorum.domain.friend.application.dto.response.LoadFriendRequestResponse;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;
import com.project.dorumdorum.domain.friend.domain.repository.FriendRequestRepository;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final UserService userService;
    private final FriendRequestRepository friendRequestRepository;

    public FriendRequest findById(Long friendRequestNo) {
        return friendRequestRepository.findById(friendRequestNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public FriendRequest saveRequest(Long fromUser, Long toUser) {
        FriendRequest newFriendRequest = FriendRequest.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(FriendRequestStatus.PENDING)
                .build();
        return friendRequestRepository.save(newFriendRequest);
    }

    public void acceptRequest(FriendRequest friendRequest) {
        friendRequest.acceptRequest();
    }

    public void rejectRequest(FriendRequest friendRequest) {
        friendRequest.rejectRequest();
    }

    public void cancelRequest(FriendRequest friendRequest) {
        friendRequestRepository.deleteById(friendRequest.getFriendRequestNo());
    }

    public List<LoadFriendRequestResponse> loadReceivedFriendRequestList(Long toUser) {
        List<FriendRequest> requests = friendRequestRepository.findByToUserAndStatus(toUser, FriendRequestStatus.PENDING);
        return requests.stream()
                .map(request -> LoadFriendRequestResponse.create(request, userService.findById(request.getFromUser())))
                .toList();
    }

    public List<LoadFriendRequestResponse> loadSentFriendRequestList(Long fromUser) {
        List<FriendRequest> requests = friendRequestRepository.findByFromUserAndStatus(fromUser, FriendRequestStatus.PENDING);
        return requests.stream()
                .map(request -> LoadFriendRequestResponse.create(request, userService.findById(request.getToUser())))
                .toList();
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
