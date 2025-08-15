package com.project.dorumdorum.domain.friend.ui;

import com.project.dorumdorum.domain.friend.application.dto.response.FriendRequestListResponse;
import com.project.dorumdorum.domain.friend.application.usecase.GetFriendRequestListUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetReceivedFriendRequestListController {

    private final GetFriendRequestListUseCase getFriendRequestListUseCase;

    @GetMapping("/api/friend-requests/received")
    public BaseResponse<List<FriendRequestListResponse>> getReceivedFriendRequestList(@CurrentUser Long userNo) {
        return BaseResponse.onSuccess(getFriendRequestListUseCase.execute(userNo));
    }
}
