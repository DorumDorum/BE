package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.DecideJoinRoomRequestUseCase;
import com.project.dorumdorum.domain.room.ui.spec.DecideJoinRoomRequestApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecideJoinRoomRequestController implements DecideJoinRoomRequestApiSpec {

    private final DecideJoinRoomRequestUseCase decideJoinRoomRequestUseCase;

    @Override
    public BaseResponse<Void> approve(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo,
            @PathVariable Long requestNo
    ) {
        decideJoinRoomRequestUseCase.approve(userNo, roomNo, requestNo);
        return BaseResponse.onSuccess();
    }

    @Override
    public BaseResponse<Void> reject(
            @CurrentUser Long userNo,
            @PathVariable Long requestNo
    ) {
        decideJoinRoomRequestUseCase.reject(userNo, requestNo);
        return BaseResponse.onSuccess();
    }
}
