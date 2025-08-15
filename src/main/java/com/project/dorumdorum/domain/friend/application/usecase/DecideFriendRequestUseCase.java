package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.service.FriendRequestService;
import com.project.dorumdorum.domain.friend.service.FriendshipService;
import com.project.dorumdorum.domain.user.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DecideFriendRequestUseCase {

    private final UserService userService;
    private final FriendRequestService friendRequestService;
    private final FriendshipService friendshipService;

    @Transactional
    public void acceptFriendRequest(Long toUser, Long requestNo) {
        // 존재하는지 검증
        userService.validateExistsById(toUser);
        // 수락 상태로 변경 후 friendship 테이블에 추가
        FriendRequest acceptedRequest = friendRequestService.acceptRequest(toUser, requestNo);
        friendshipService.addFriendship(acceptedRequest.getFromUser(), acceptedRequest.getToUser());
    }

    @Transactional
    public void rejectFriendRequest(Long toUser, Long requestNo) {
        userService.validateExistsById(toUser);

        friendRequestService.rejectRequest(toUser, requestNo);
    }
}
