package com.project.dorumdorum.domain.friend.ui;

import com.project.dorumdorum.domain.friend.application.usecase.DeleteFriendshipUseCase;

import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteFriendshipController {

    private final DeleteFriendshipUseCase deleteFriendshipUseCase;

    @DeleteMapping("/api/friend-requests/{friendUserNo}")
    public BaseResponse<Void> deleteFriendship(
            @CurrentUser Long userNo,
            @PathVariable Long friendUserNo
    ) {
        deleteFriendshipUseCase.execute(userNo, friendUserNo);
        return BaseResponse.onSuccess();
    }
}
