package com.project.dorumdorum.domain.friend.application.usecase;


import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.service.FriendRequestService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelFriendRequestUseCase {

    private final UserService userService;
    private final FriendRequestService friendRequestService;


    @Transactional
    public void execute(Long userNo, Long friendRequestNo) {
        // 유저 존재하는지 조회
        userService.validateExistsById(userNo);

        // 요청 존재하는지 조회
        FriendRequest friendRequest = friendRequestService.findById(friendRequestNo);

        if(!friendRequest.isSender(userNo))
            throw new RestApiException(GlobalErrorStatus.REQUEST_NOT_SENDER);

        if(!friendRequest.isPendingStatus())
            throw new RestApiException(GlobalErrorStatus.REQUEST_NOT_PENDING);

        friendRequestService.cancelRequest(friendRequest);
    }
}
