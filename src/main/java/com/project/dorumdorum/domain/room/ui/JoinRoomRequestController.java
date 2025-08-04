package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.application.dto.usecase.JoinRoomRequestUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class JoinRoomRequestController {

    private final JoinRoomRequestUseCase joinRoomRequestUseCase;

    @PostMapping("/api/room/{roomNo}/join")
    public BaseResponse<Void> join(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo,
            @RequestBody @Valid JoinRoomRequest request
    ) {
        joinRoomRequestUseCase.execute(userNo, roomNo, request);
        return BaseResponse.onSuccess();
    }
}
