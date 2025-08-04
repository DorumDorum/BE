package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.InviteRoomRequest;
import com.project.dorumdorum.domain.room.application.dto.usecase.InviteRoomRequestUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InviteRoomRequestController {

    private final InviteRoomRequestUseCase inviteRoomRequestUseCase;

    @PostMapping("/api/room/{roomNo}/invite-request/user/{toUser}")
    public BaseResponse<Void> invite(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo,
            @PathVariable Long toUser,
            @RequestBody InviteRoomRequest request
    ) {
        inviteRoomRequestUseCase.execute(userNo, roomNo, toUser, request);
        return BaseResponse.onSuccess();
    }
}