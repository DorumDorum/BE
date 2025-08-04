package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.usecase.JoinRoomDecisionUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinRequestDecisionController {

    private final JoinRoomDecisionUseCase joinRoomDecisionUseCase;

    @PostMapping("/api/room/{roomNo}/join-request/{requestNo}/approve")
    public BaseResponse<Void> approve(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo,
            @PathVariable Long requestNo
    ) {
        joinRoomDecisionUseCase.approve(userNo, roomNo, requestNo);
        return BaseResponse.onSuccess();
    }

    @PostMapping("/api/join-request/{requestNo}/reject")
    public BaseResponse<Void> reject(
            @CurrentUser Long userNo,
            @PathVariable Long requestNo
    ) {
        joinRoomDecisionUseCase.reject(userNo, requestNo);
        return BaseResponse.onSuccess();
    }
}
