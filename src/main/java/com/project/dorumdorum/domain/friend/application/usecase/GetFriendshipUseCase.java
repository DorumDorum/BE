package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.application.dto.response.GetFriendListResponse;
import com.project.dorumdorum.domain.friend.service.FriendshipService;
import com.project.dorumdorum.domain.user.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetFriendshipUseCase {

    private final UserService userService;
    private final FriendshipService friendshipService;

    public List<GetFriendListResponse> execute(long userNo) {
        userService.validateExistsById(userNo);
        return friendshipService.getFriendList(userNo);
    }
}