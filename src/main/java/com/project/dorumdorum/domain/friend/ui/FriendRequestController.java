package com.project.dorumdorum.domain.friend.ui;

import com.project.dorumdorum.domain.friend.application.dto.request.FriendRequestRequest;
import com.project.dorumdorum.domain.friend.application.usecase.FriendRequestUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestUseCase friendRequestUseCase;

    @PostMapping("/api/friend-request")
    public BaseResponse<Void> sendFriendRequest(
            @CurrentUser Long userNo,
            @RequestBody @Valid FriendRequestRequest request
    ) {
        friendRequestUseCase.execute(userNo, request);
        return BaseResponse.onSuccess();
    }



































}
