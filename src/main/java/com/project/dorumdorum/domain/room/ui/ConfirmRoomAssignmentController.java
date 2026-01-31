package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.ConfirmRoomAssignmentUseCase;
import com.project.dorumdorum.domain.room.ui.spec.ConfirmRoomAssignmentApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfirmRoomAssignmentController implements ConfirmRoomAssignmentApiSpec {

    private final ConfirmRoomAssignmentUseCase confirmRoomAssignmentUseCase;

    @Override
    public BaseResponse<Void> confirm(
            @CurrentUser Long userNo,
            @RequestParam Long roomNo
    ) {
        confirmRoomAssignmentUseCase.execute(userNo, roomNo);
        return BaseResponse.onSuccess(null);
    }
}
