package com.project.dorumdorum.domain.friend.ui;

import com.project.dorumdorum.domain.friend.application.dto.request.LoadFriendRequest;
import com.project.dorumdorum.domain.friend.application.dto.response.LoadFriendRequestResponse;
import com.project.dorumdorum.domain.friend.application.usecase.LoadFriendRequestsUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadFriendRequestsController {

    private final LoadFriendRequestsUseCase loadFriendRequestsUseCase;

    @PostMapping("/api/friend-requests")
    public BaseResponse<List<LoadFriendRequestResponse>> getReceivedFriendRequestList(
            @CurrentUser Long userNo,
            @RequestBody @Valid LoadFriendRequest loadFriendRequest
    ) {
        return BaseResponse.onSuccess(loadFriendRequestsUseCase.execute(userNo, loadFriendRequest));
    }
}
