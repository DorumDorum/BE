package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.application.dto.response.LoadFriendshipResponse;
import com.project.dorumdorum.domain.friend.service.FriendshipService;
import com.project.dorumdorum.domain.user.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadFriendshipUseCase {

    private final UserService userService;
    private final FriendshipService friendshipService;

    public List<LoadFriendshipResponse> execute(long userNo) {
        userService.validateExistsById(userNo);

        return friendshipService.loadFriendList(userNo);
    }
}