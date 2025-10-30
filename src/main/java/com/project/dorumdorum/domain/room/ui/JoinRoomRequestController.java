package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.application.usecase.JoinRoomRequestUseCase;
import com.project.dorumdorum.domain.room.ui.spec.JoinRoomRequestApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class JoinRoomRequestController implements JoinRoomRequestApiSpec {

    private final JoinRoomRequestUseCase joinRoomRequestUseCase;

    @Override
    public BaseResponse<Void> join(
            @CurrentUser Long userNo,
            @PathVariable Long roomNo,
            @RequestBody @Valid JoinRoomRequest request
    ) {
        joinRoomRequestUseCase.execute(userNo, roomNo, request);
        return BaseResponse.onSuccess();
    }
}
