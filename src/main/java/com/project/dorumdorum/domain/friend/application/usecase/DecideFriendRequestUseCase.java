package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.service.FriendRequestService;
import com.project.dorumdorum.domain.user.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DecideFriendRequestUseCase {

    private final UserService userService;
    private final FriendRequestService friendRequestService;

    @Transactional
    public void acceptFriendRequest(Long toUser, Long requestNo) {
        userService.validateExistsById(toUser);

        friendRequestService.acceptRequest(toUser, requestNo);
    }

    @Transactional
    public void rejectFriendRequest(Long toUser, Long requestNo) {
        userService.validateExistsById(toUser);

        friendRequestService.rejectRequest(toUser, requestNo);
    }
}
