package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.usecase.InitiateRoomConfirmationUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InitiateRoomConfirmationController {

    private final InitiateRoomConfirmationUseCase initiateRoomConfirmationUseCase;

    @PostMapping("/api/room/{roomNo}")
    public BaseResponse<Void> init(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo
    ) {
        initiateRoomConfirmationUseCase.execute(userNo, roomNo);
        return BaseResponse.onSuccess();
    }
}
