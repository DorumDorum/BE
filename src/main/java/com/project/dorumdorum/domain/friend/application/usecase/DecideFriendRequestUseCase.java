package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.service.FriendRequestService;
import com.project.dorumdorum.domain.friend.service.FriendshipService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.REQUEST_NOT_RECEIVER;
import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.REQUEST_NOT_PENDING;

@Service
@RequiredArgsConstructor
public class DecideFriendRequestUseCase {

    private final UserService userService;
    private final FriendRequestService friendRequestService;
    private final FriendshipService friendshipService;

    @Transactional
    public void acceptFriendRequest(Long toUser, Long friendRequestNo) {
        userService.validateExistsById(toUser);
        
        // 친구 요청 객체 받기
        FriendRequest friendRequest = friendRequestService.findById(friendRequestNo);

        // 친구 요청 검증 (현재 유저가 수락할 권한이 있는지)
        if (!friendRequest.isReceiver(toUser))
            throw new RestApiException(REQUEST_NOT_RECEIVER);

        // 친구 요청 검증 (친구추가 요청이 PENDING 상태인지)
        if (!friendRequest.isPendingStatus())
            throw new RestApiException(REQUEST_NOT_PENDING);

        // 친구 수락
        friendRequestService.acceptRequest(friendRequest);

        // Friendship 테이블에 친구 등록
        friendshipService.addFriendship(friendRequest.getFromUser(), friendRequest.getToUser());
    }

    @Transactional
    public void rejectFriendRequest(Long toUser, Long friendRequestNo) {
        userService.validateExistsById(toUser);

        // 친구 요청 객체 받기
        FriendRequest friendRequest = friendRequestService.findById(friendRequestNo);

        // 친구 요청 검증 (현재 유저가 수락할 권한이 있는지)
        if (!friendRequest.isReceiver(toUser))
            throw new RestApiException(REQUEST_NOT_RECEIVER);

        // 친구 요청 검증 (친구추가 요청이 PENDING 상태인지)
        if (!friendRequest.isPendingStatus())
            throw new RestApiException(REQUEST_NOT_PENDING);

        // 친구 거절
        friendRequestService.rejectRequest(friendRequest);
    }
}
