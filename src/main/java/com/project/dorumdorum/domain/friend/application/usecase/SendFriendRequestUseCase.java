package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.service.FriendRequestService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.domain.friend.application.dto.request.FriendRequestRequest;
import com.project.dorumdorum.domain.user.domain.entity.User;

import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendFriendRequestUseCase {

    private final UserService userService;
    private final FriendRequestService friendshipRequestService;

    @Transactional
    public void execute(Long userNo, FriendRequestRequest request) {
        User fromUser = userService.findById(userNo);
        User toUser = userService.findByEmail(request.email());

        if(fromUser.equals(toUser)) { throw new RestApiException(GlobalErrorStatus.FRIEND_SELF_REQUEST); }
        if(friendshipRequestService.existFriendRequestByFromUser(fromUser)) { throw new RestApiException(GlobalErrorStatus.DUPLICATE_FRIEND_REQUEST); }
        if(friendshipRequestService.areAlreadyFriends(fromUser, toUser)) { throw new RestApiException(GlobalErrorStatus.ALREADY_FRIEND); }

        friendshipRequestService.saveRequest(fromUser, toUser);
    }

}
