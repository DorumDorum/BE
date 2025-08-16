package com.project.dorumdorum.domain.friend.ui;


import com.project.dorumdorum.domain.friend.application.usecase.CancelFriendRequestUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CancelFriendRequestController {

    private final CancelFriendRequestUseCase cancelFriendRequestUseCase;

    @DeleteMapping("/api/friend-requests/{friendRequestNo}")
    public BaseResponse<Void> cancelFriendRequest(
            @CurrentUser Long userNo,
            @PathVariable Long friendRequestNo
    ) {
        cancelFriendRequestUseCase.execute(userNo, friendRequestNo);
        return BaseResponse.onSuccess();
    }
}
