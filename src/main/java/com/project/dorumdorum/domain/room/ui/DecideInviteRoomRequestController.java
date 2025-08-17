package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.usecase.DecideInviteRoomRequestUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecideInviteRoomRequestController {

    private final DecideInviteRoomRequestUseCase decideInviteRoomRequestUseCase;

    @PostMapping("/api/rooms/{roomNo}/invite-request/{requestNo}/approve")
    public BaseResponse<Void> approve(
            @CurrentUser Long userNo,
            @PathVariable Long requestNo,
            @PathVariable Long roomNo
    ) {
        decideInviteRoomRequestUseCase.approve(userNo, requestNo, roomNo);
        return BaseResponse.onSuccess();
    }
}
