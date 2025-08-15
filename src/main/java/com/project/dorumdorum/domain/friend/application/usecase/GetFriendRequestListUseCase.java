package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.application.dto.response.FriendRequestListResponse;
import com.project.dorumdorum.domain.friend.service.FriendRequestService;

import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetFriendRequestListUseCase {

    private final UserService userService;
    private final FriendRequestService friendRequestService;

    public List<FriendRequestListResponse> execute(Long userNo) {
        User toUser = userService.findById(userNo);

        return friendRequestService.getReceivedFriendRequestList(toUser);
    }
}
