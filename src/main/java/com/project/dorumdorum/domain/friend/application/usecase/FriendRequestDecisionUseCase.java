package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.service.FriendRequestService;

import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendRequestDecisionUseCase {

    private final UserService userService;
    private final FriendRequestService friendRequestService;

    @Transactional
    public void acceptFriendRequest(Long toUser, Long requestNo) {
        User user = userService.findById(toUser);

        friendRequestService.acceptRequest(user, requestNo);
    }

    @Transactional
    public void rejectFriendRequest(Long toUser, Long requestNo) {
        User user = userService.findById(toUser);

        friendRequestService.rejectRequest(user, requestNo);
    }
}
