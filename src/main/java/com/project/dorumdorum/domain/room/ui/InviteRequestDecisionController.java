package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.usecase.InviteRequestDecisionUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InviteRequestDecisionController {

    private final InviteRequestDecisionUseCase inviteRequestDecisionUseCase;

    @PostMapping("/api/room/{roomNo}/invite-request/{requestNo}/approve")
    public BaseResponse<Void> approve(
            @CurrentUser Long userNo,
            @PathVariable Long requestNo,
            @PathVariable Long roomNo
    ) {
        inviteRequestDecisionUseCase.approve(userNo, requestNo, roomNo);
        return BaseResponse.onSuccess();
    }
}
