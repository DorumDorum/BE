package com.project.dorumdorum.domain.friend.ui;


import com.project.dorumdorum.domain.friend.application.usecase.DecideFriendRequestUseCase;
import com.project.dorumdorum.domain.friend.application.usecase.GetFriendRequestListUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-requests")
public class DecideFriendRequestController {

    private final GetFriendRequestListUseCase getFriendRequestListUseCase;
    private final DecideFriendRequestUseCase friendRequestDecisionUseCase;



    @PostMapping("/{requestNo}/accept")
    public BaseResponse<Void> acceptFriendRequest(
            @CurrentUser Long userNo,
            @PathVariable Long requestNo
    ) {
        friendRequestDecisionUseCase.acceptFriendRequest(userNo, requestNo);
        return BaseResponse.onSuccess();
    }

    @PostMapping("/{requestNo}/reject")
    public BaseResponse<Void> rejectFriendRequest(
            @CurrentUser Long userNo,
            @PathVariable Long requestNo
    ) {
        friendRequestDecisionUseCase.rejectFriendRequest(userNo, requestNo);
        return BaseResponse.onSuccess();
    }
}
