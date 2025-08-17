package com.project.dorumdorum.domain.friend.application.usecase;

import com.project.dorumdorum.domain.friend.application.dto.request.FriendRequestType;
import com.project.dorumdorum.domain.friend.application.dto.request.LoadFriendRequest;
import com.project.dorumdorum.domain.friend.application.dto.response.LoadFriendRequestResponse;
import com.project.dorumdorum.domain.friend.service.FriendRequestService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadFriendRequestsUseCase {

    private final UserService userService;
    private final FriendRequestService friendRequestService;

    public List<LoadFriendRequestResponse> execute(Long userNo, LoadFriendRequest loadFriendRequest) {
        userService.validateExistsById(userNo);

        if(loadFriendRequest.friendRequestType() == FriendRequestType.RECEIVED) {
            return friendRequestService.loadReceivedFriendRequestList(userNo);
        }
        else if(loadFriendRequest.friendRequestType() == FriendRequestType.SENT) {
            return friendRequestService.loadSentFriendRequestList(userNo);
        }
        else
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
    }
}
