package com.project.dorumdorum.domain.friend.ui;


import com.project.dorumdorum.domain.friend.application.dto.response.FriendRequestListResponse;
import com.project.dorumdorum.domain.friend.application.usecase.GetSentFriendRequestUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetSentFriendRequestController {

    private final GetSentFriendRequestUseCase getSentFriendRequestUseCase;

    @GetMapping("/api/friend-requests/sent")
    public BaseResponse<List<FriendRequestListResponse>> getSentFriendRequestList(@CurrentUser Long userNo) {
        return BaseResponse.onSuccess(getSentFriendRequestUseCase.execute(userNo));
    }
}
