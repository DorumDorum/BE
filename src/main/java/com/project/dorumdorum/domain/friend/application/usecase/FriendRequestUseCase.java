package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.service.FriendRequestService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.domain.friend.application.dto.request.FriendRequestRequest;
import com.project.dorumdorum.domain.user.domain.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendRequestUseCase {

    private final UserService userService;
    private final FriendRequestService friendshipRequestService;

    @Transactional
    public void execute(Long userNo, FriendRequestRequest request) {
        User fromUser = userService.findById(userNo);
        User toUser = userService.findByEmail(request.email());

        if(fromUser.equals(toUser)) { /* 요청을 보낸 이와 받는 이가 같을 때 exception 처리*/ }
        friendshipRequestService.saveRequest(fromUser, toUser);
    }

}
