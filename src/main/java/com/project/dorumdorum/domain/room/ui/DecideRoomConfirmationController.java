package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.DecideRoomConfirmationUseCase;
import com.project.dorumdorum.domain.room.ui.spec.DecideRoomConfirmationApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecideRoomConfirmationController implements DecideRoomConfirmationApiSpec {

    private final DecideRoomConfirmationUseCase decideRoomConfirmationUseCase;

    @Override
    public BaseResponse<Void> approve(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo
    ) {
        decideRoomConfirmationUseCase.approve(userNo, roomNo);
        return BaseResponse.onSuccess();
    }

    @Override
    public BaseResponse<Void> reject(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo
    ) {
        decideRoomConfirmationUseCase.approve(userNo, roomNo);
        return BaseResponse.onSuccess();
    }
}
