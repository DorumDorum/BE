package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.usecase.DecideRoomConfirmationUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecideRoomConfirmationController {

    private final DecideRoomConfirmationUseCase decideRoomConfirmationUseCase;

    @PostMapping("/api/rooms/{roomNo}/confirm/approve")
    public BaseResponse<Void> approve(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo
    ) {
        decideRoomConfirmationUseCase.approve(userNo, roomNo);
        return BaseResponse.onSuccess();
    }

    @PostMapping("/api/rooms/{roomNo}/confirm/reject")
    public BaseResponse<Void> reject(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo
    ) {
        decideRoomConfirmationUseCase.approve(userNo, roomNo);
        return BaseResponse.onSuccess();
    }
}
