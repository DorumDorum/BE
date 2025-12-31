package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.DecideInvitationRequestUseCase;
import com.project.dorumdorum.domain.room.ui.spec.DecideInvitationRequestApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecideInvitationRequestController implements DecideInvitationRequestApiSpec {

    private final DecideInvitationRequestUseCase decideInvitationRequestUseCase;

    @Override
    public BaseResponse<Void> approve(
            @CurrentUser Long userNo,
            @PathVariable Long requestNo,
            @PathVariable Long roomNo
    ) {
        decideInvitationRequestUseCase.approve(userNo, requestNo, roomNo);
        return BaseResponse.onSuccess();
    }
}
