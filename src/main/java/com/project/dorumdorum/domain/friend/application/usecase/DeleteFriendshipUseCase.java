package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.service.FriendshipService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteFriendshipUseCase {

    private final UserService userService;
    private final FriendshipService friendshipService;

    @Transactional
    public void execute(Long userNo, Long friendUserNo) {
        userService.validateExistsById(friendUserNo);

        if(userNo.equals(friendUserNo))
            throw new RestApiException(GlobalErrorStatus.FRIEND_SELF_REQUEST);

        if (!friendshipService.areAlreadyFriends(userNo, friendUserNo))
            throw new RestApiException(GlobalErrorStatus.FRIENDSHIP_NOT_FOUND);

        friendshipService.deleteFriendship(userNo, friendUserNo);
    }
}
