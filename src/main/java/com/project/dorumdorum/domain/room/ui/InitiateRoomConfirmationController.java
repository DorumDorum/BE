package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.InitiateRoomConfirmationUseCase;
import com.project.dorumdorum.domain.room.ui.spec.InitiateRoomConfirmationApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InitiateRoomConfirmationController implements InitiateRoomConfirmationApiSpec {

    private final InitiateRoomConfirmationUseCase initiateRoomConfirmationUseCase;

    @Override
    public BaseResponse<Void> init(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo
    ) {
        initiateRoomConfirmationUseCase.execute(userNo, roomNo);
        return BaseResponse.onSuccess();
    }
}
