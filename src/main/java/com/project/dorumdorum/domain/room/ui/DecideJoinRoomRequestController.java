package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.usecase.DecideJoinRoomRequestUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecideJoinRoomRequestController {

    private final DecideJoinRoomRequestUseCase decideJoinRoomRequestUseCase;

    @PostMapping("/api/rooms/{roomNo}/join-request/{requestNo}/approve")
    public BaseResponse<Void> approve(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo,
            @PathVariable Long requestNo
    ) {
        decideJoinRoomRequestUseCase.approve(userNo, roomNo, requestNo);
        return BaseResponse.onSuccess();
    }

    @PostMapping("/api/join-request/{requestNo}/reject")
    public BaseResponse<Void> reject(
            @CurrentUser Long userNo,
            @PathVariable Long requestNo
    ) {
        decideJoinRoomRequestUseCase.reject(userNo, requestNo);
        return BaseResponse.onSuccess();
    }
}
